import React, { useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { css } from '@emotion/react';
import * as S from './MeetingCreatePage.styled';
import Footer from 'components/layouts/Footer';
import Input from 'components/@shared/Input';
import MemberAddInput from 'components/MemberAddInput';
import InputHint from 'components/@shared/InputHint';
import useForm from 'hooks/useForm';
import useMutation from 'hooks/useMutation';
import useQuerySelectItems from 'hooks/useQuerySelectItems';
import { UserQueryWithKeywordResponse } from 'types/userType';
import { userContext, UserContextValues } from 'contexts/userContext';
import { createMeetingApi } from 'apis/meetingApis';

const MAX_SELECTED_USER_COUNT = 30;

const MeetingCreatePage = () => {
  const navigate = useNavigate();
  const { accessToken } = useContext(userContext) as UserContextValues;
  const { errors, isSubmitting, onSubmit, register } = useForm();
  const {
    queryResult,
    selectedItems,
    queryWithKeyword,
    selectItem,
    unselectItem,
    clearQueryResult,
  } = useQuerySelectItems<UserQueryWithKeywordResponse>('/users?keyword=', {
    wait: 150,
    maxSelectCount: MAX_SELECTED_USER_COUNT,
  });
  const isParticipantSelected = selectedItems.length > 0;

  const meetingCreateMutation = useMutation(createMeetingApi, {
    onSuccess: ({ body: { id } }) => {
      alert('모임 생성을 완료했습니다.');
      navigate(`/meeting/${id}`);
    },
    onError: () => {
      alert('모임 생성을 실패했습니다.');
    },
  });

  const handleCreateMeetingSubmit: React.FormEventHandler<
    HTMLFormElement
  > = async ({ currentTarget }) => {
    if (!isParticipantSelected) {
      return;
    }

    const userIds = selectedItems.map(({ id }) => id);

    const formData = new FormData(currentTarget);
    const formDataObject = {
      ...Object.fromEntries(formData.entries()),
      userIds,
    };

    meetingCreateMutation.mutate({
      accessToken,
      formDataObject,
    });
  };

  return (
    <>
      <S.Layout>
        <S.Form
          id="meeting-create-form"
          {...onSubmit(handleCreateMeetingSubmit)}
        >
          <S.FieldBox>
            <S.Label>
              모임명
              <Input
                type="text"
                {...register('name', { maxLength: 50, required: true })}
              />
            </S.Label>
            <InputHint
              isShow={Boolean(errors['name']) && errors['name'] !== ''}
              message={errors['name']}
            />
          </S.FieldBox>
          <S.FieldBox>
            <S.Label>
              <S.AddMemberParagraph>
                멤버 추가하기
                <span>
                  {selectedItems.length}/{MAX_SELECTED_USER_COUNT}
                </span>
              </S.AddMemberParagraph>
              <MemberAddInput
                css={css`
                  width: 100%;
                `}
                name="searchMember"
                placeholder="닉네임 또는 이메일로 검색하세요."
                disabled={selectedItems.length >= MAX_SELECTED_USER_COUNT}
                queryResult={queryResult}
                selectedItems={selectedItems}
                queryWithKeyword={queryWithKeyword}
                selectItem={selectItem}
                unselectItem={unselectItem}
                clearQueryResult={clearQueryResult}
              />
            </S.Label>
          </S.FieldBox>
          <InputHint
            isShow={!isParticipantSelected}
            message="참여자가 선택되지 않았습니다."
          />
        </S.Form>
        <S.MeetingCreateButton
          form="meeting-create-form"
          type="submit"
          disabled={isSubmitting}
        >
          모임 생성하기
        </S.MeetingCreateButton>
      </S.Layout>
      <Footer />
    </>
  );
};

export default MeetingCreatePage;
