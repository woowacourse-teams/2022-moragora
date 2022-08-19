import React, { useContext } from 'react';
import { Navigate, useOutletContext, useParams } from 'react-router-dom';
import { css } from '@emotion/react';
import * as S from './CalendarPage.styled';
import Button from 'components/@shared/Button';
import Calendar from 'components/@shared/Calendar';
import DialogButton from 'components/@shared/DialogButton';
import Input from 'components/@shared/Input';
import { CalendarContext } from 'contexts/calendarContext';
import { userContext, UserContextValues } from 'contexts/userContext';
import useForm from 'hooks/useForm';
import useMutation from 'hooks/useMutation';
import useQuery from 'hooks/useQuery';
import { dateToFormattedString } from 'utils/timeUtil';
import {
  createEventsApi,
  deleteEventsApi,
  getEventsApi,
  getUpcomingEventApi,
} from 'apis/eventApis';
import { getMeetingData } from 'apis/meetingApis';
import Spinner from 'components/@shared/Spinner';
import { QueryState } from 'types/queryType';
import ErrorIcon from 'components/@shared/ErrorIcon';
import { MeetingEvent } from 'types/eventType';

const CalendarPage = () => {
  const { id: meetingId } = useParams();
  const user = useContext(userContext) as UserContextValues;

  if (!meetingId) {
    return <Navigate to="/error" />;
  }

  const { meetingQuery } = useOutletContext<{
    meetingQuery: QueryState<typeof getMeetingData>;
    upcomingEventQuery: QueryState<typeof getUpcomingEventApi>;
    totalTardyCount: number;
    upcomingEventNotExist: boolean;
  }>();

  const eventsQuery = useQuery(
    ['events'],
    getEventsApi(meetingId, user.accessToken),
    {
      onSuccess: ({ body: { events } }) => {
        setSavedEvents(events);
      },
      onError: (error) => {
        alert(error.message);
      },
    }
  );

  const createEventsMutation = useMutation(
    createEventsApi(meetingId, user.accessToken),
    {
      onSuccess: () => {
        eventsQuery.refetch();
        alert('일정을 생성했습니다.');
      },
      onError: (error) => {
        alert(error.message);
      },
    }
  );

  const removeEventsMutation = useMutation(
    deleteEventsApi(meetingId, user.accessToken),
    {
      onSuccess: () => {
        eventsQuery.refetch();
        alert('일정을 삭제했습니다.');
      },
      onError: (error) => {
        eventsQuery.refetch();
        alert(error.message);
      },
    }
  );

  const {
    events,
    selectedDates,
    clearSelectedDates,
    updateEvents,
    setSavedEvents,
  } = useContext(CalendarContext);
  const { values, errors, onSubmit, register } = useForm();

  const handleupdateEventsSubmit: React.FormEventHandler<HTMLFormElement> = ({
    currentTarget,
  }) => {
    const formData = new FormData(currentTarget);
    const formDataObject = Object.fromEntries(formData.entries()) as Pick<
      MeetingEvent,
      'meetingStartTime' | 'meetingEndTime'
    >;

    updateEvents(
      selectedDates.map((date) => ({
        date: dateToFormattedString(date),
        ...formDataObject,
      }))
    );
    clearSelectedDates();
  };

  const handleRemoveEventsButtonClick = () => {
    removeEventsMutation.mutate({
      dates: selectedDates.map((date) => dateToFormattedString(date)),
    });
    clearSelectedDates();
  };

  const handleClickSaveEventsButtonClick = () => {
    createEventsMutation.mutate({
      events,
    });
  };

  if (eventsQuery.isLoading) {
    return (
      <>
        <S.Layout>
          <S.SpinnerBox>
            <Spinner />
          </S.SpinnerBox>
        </S.Layout>
      </>
    );
  }

  if (eventsQuery.isError) {
    return (
      <>
        <S.Layout>
          <S.SpinnerBox>
            <ErrorIcon />
          </S.SpinnerBox>
        </S.Layout>
      </>
    );
  }

  return (
    <S.Layout>
      <S.CalendarBox>
        <Calendar readOnly={!meetingQuery.data?.body.isLoginUserMaster} />
      </S.CalendarBox>
      {meetingQuery.data?.body.isLoginUserMaster && (
        <S.CalenderControlBox>
          <S.Form
            id="add-events-form"
            {...onSubmit(handleupdateEventsSubmit)}
            css={css`
              padding: 0 0.75rem;
            `}
          >
            <S.FieldGroupBox>
              <S.FieldBox>
                <S.Label>
                  시작 시간
                  <Input
                    type="time"
                    {...register('meetingStartTime', {
                      onClick: (e) => {
                        const currentTarget =
                          e.currentTarget as HTMLInputElement & {
                            showPicker: () => void;
                          };

                        currentTarget.showPicker();
                      },
                      watch: true,
                    })}
                  />
                </S.Label>
              </S.FieldBox>
              <S.FieldBox>
                <S.Label>
                  마감 시간
                  <Input
                    type="time"
                    {...register('meetingEndTime', {
                      onClick: (e) => {
                        const currentTarget =
                          e.currentTarget as HTMLInputElement & {
                            showPicker: () => void;
                          };

                        currentTarget.showPicker();
                      },
                      watch: true,
                    })}
                    disabled={errors['meetingStartTime'] !== ''}
                  />
                </S.Label>
              </S.FieldBox>
              <DialogButton
                variant="confirm"
                css={css`
                  height: 3rem;
                  align-self: flex-end;
                `}
                type="submit"
                form="add-events-form"
                disabled={
                  !values['meetingStartTime'] ||
                  !values['meetingEndTime'] ||
                  selectedDates.length === 0
                }
              >
                추가
              </DialogButton>
            </S.FieldGroupBox>
          </S.Form>
          <S.ButtonBox>
            <Button type="button" onClick={handleRemoveEventsButtonClick}>
              삭제
            </Button>
            <Button
              type="button"
              onClick={handleClickSaveEventsButtonClick}
              disabled={events.length === 0}
            >
              저장
            </Button>
          </S.ButtonBox>
        </S.CalenderControlBox>
      )}
    </S.Layout>
  );
};

export default CalendarPage;
