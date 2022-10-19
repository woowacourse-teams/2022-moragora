import React, { useContext } from 'react';
import {
  Navigate,
  useNavigate,
  useOutletContext,
  useParams,
} from 'react-router-dom';
import { css } from '@emotion/react';
import * as S from './MeetingConfigPage.styled';
import Button from 'components/@shared/Button';
import Input from 'components/@shared/Input';
import { userContext, UserContextValues } from 'contexts/userContext';
import useForm from 'hooks/useForm';
import useMutation from 'hooks/useMutation';
import {
  deleteMeetingApi,
  getMeetingData,
  updateMeetingNameApi,
  leaveMeetingApi,
} from 'apis/meetingApis';
import { getUpcomingEventApi } from 'apis/eventApis';
import { QueryState } from 'types/queryType';
import MasterAssignSection from './MasterAssignSection';
import InputHint from 'components/@shared/InputHint';
import { MeetingNameUpdateRequestBody } from 'types/meetingType';

const MeetingConfigPage = () => {
  const navigate = useNavigate();
  const { id: meetingId } = useParams();
  const userState = useContext(userContext) as UserContextValues;

  if (!meetingId) {
    return <Navigate to="/error" />;
  }

  const { errors, onSubmit, register } = useForm();
  const { meetingQuery } = useOutletContext<{
    meetingQuery: QueryState<typeof getMeetingData>;
    upcomingEventQuery: QueryState<typeof getUpcomingEventApi>;
    totalTardyCount: number;
    upcomingEventNotExist: boolean;
  }>();
  const meetingNameUpdateMutation = useMutation(
    updateMeetingNameApi(meetingId, userState.accessToken),
    {
      onSuccess: () => {
        meetingQuery.refetch();
        alert('모임명이 수정되었습니다.');
      },
      onError: (e) => {
        alert(e.message);
      },
    }
  );
  const meetingDeleteMutation = useMutation(
    deleteMeetingApi(meetingId, userState.accessToken),
    {
      onSuccess: () => {
        alert('모임이 삭제되었습니다.');
        navigate('/');
      },
      onError: (e) => {
        alert(e.message);
      },
    }
  );
  const meetingLeaveMutation = useMutation(
    leaveMeetingApi(meetingId, userState.accessToken),
    {
      onSuccess: () => {
        alert('모임을 나갔습니다.');
        navigate('/');
      },
      onError: (e) => {
        alert(e.message);
      },
    }
  );

  const handleMeetingNameSubmitValid: React.FormEventHandler<
    HTMLFormElement
  > = ({ currentTarget }) => {
    const formData = new FormData(currentTarget);
    const formDataObject = Object.fromEntries(
      formData.entries()
    ) as MeetingNameUpdateRequestBody;

    meetingNameUpdateMutation.mutate(formDataObject);
  };

  const handleMeetingDeleteClick: React.MouseEventHandler<
    HTMLButtonElement
  > = ({ currentTarget }) => {
    meetingDeleteMutation.mutate({});
  };

  const handleMeetingLeaveClick: React.MouseEventHandler<
    HTMLButtonElement
  > = () => {
    meetingLeaveMutation.mutate({});
  };

  if (meetingQuery.data?.body.isLoginUserMaster) {
    return (
      <S.Layout>
        <S.Form
          id="meeting-name-update-form"
          {...onSubmit(handleMeetingNameSubmitValid)}
        >
          <S.FieldBox>
            <S.Label>
              모임명
              <Input
                type="text"
                {...register('name', {
                  defaultValue: meetingQuery.data.body.name,
                  placeholder: meetingQuery.data.body.name,
                  maxLength: 50,
                  required: true,
                })}
              />
            </S.Label>
            <InputHint isShow={!!errors['name']} message={errors['name']} />
          </S.FieldBox>
          <Button form="meeting-name-update-form">모임명 변경하기</Button>
        </S.Form>
        <div
          css={css`
            padding: 0.75rem;
          `}
        >
          <MasterAssignSection />
        </div>
        <div
          css={css`
            padding: 0.75rem;
          `}
        >
          <Button onClick={handleMeetingDeleteClick}>모임 삭제하기</Button>
        </div>
      </S.Layout>
    );
  }

  return (
    <S.Layout>
      <div
        css={css`
          padding: 0.75rem;
        `}
      >
        <Button onClick={handleMeetingLeaveClick}>모임 나가기</Button>
      </div>
    </S.Layout>
  );
};

export default MeetingConfigPage;
