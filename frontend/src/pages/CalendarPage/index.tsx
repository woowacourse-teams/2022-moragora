import React, { useContext, useState } from 'react';
import { Navigate, useOutletContext, useParams } from 'react-router-dom';
import Button from 'components/@shared/Button';
import Calendar from 'components/@shared/Calendar';
import { CalendarContext } from 'contexts/calendarContext';
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
import TimePicker from 'components/@shared/TimePicker';
import * as S from './CalendarPage.styled';

const CalendarPage = () => {
  const { id: meetingId } = useParams();

  if (!meetingId) {
    return <Navigate to="/error" />;
  }

  const currentDate = new Date();
  const { selectedDates, clearSelectedDates, setSavedEvents } =
    useContext(CalendarContext);
  const [meetingStartTime, setMeetingStartTime] = useState<
    Partial<{
      hour: number;
      minute: number;
    }>
  >();
  const [meetingEndTime, setMeetingEndTime] = useState<
    Partial<{
      hour: number;
      minute: number;
    }>
  >();

  const { meetingQuery } = useOutletContext<{
    meetingQuery: QueryState<typeof getMeetingData>;
    upcomingEventQuery: QueryState<typeof getUpcomingEventApi>;
    totalTardyCount: number;
    upcomingEventNotExist: boolean;
  }>();

  const eventsQuery = useQuery(['events'], getEventsApi(meetingId), {
    onSuccess: ({ data: { events: queriedEvents } }) => {
      setSavedEvents(queriedEvents);
    },
    onError: (error) => {
      alert(error.message);
    },
  });

  const createEventsMutation = useMutation(createEventsApi(meetingId), {
    onSuccess: () => {
      eventsQuery.refetch();
      alert('일정을 생성했습니다.');
    },
    onError: (error) => {
      alert(error.message);
    },
  });

  const removeEventsMutation = useMutation(deleteEventsApi(meetingId), {
    onSuccess: () => {
      eventsQuery.refetch();
      alert('일정을 삭제했습니다.');
    },
    onError: (error) => {
      eventsQuery.refetch();
      alert(error.message);
    },
  });

  const handleUpdateEventsSubmit: React.FormEventHandler<HTMLFormElement> = (
    e
  ) => {
    e.preventDefault();

    const formData = new FormData(e.currentTarget);
    const formDataObject = Object.fromEntries(
      formData.entries()
    ) as unknown as {
      'meetingStartTime-hour': number;
      'meetingStartTime-minute': number;
      'meetingEndTime-hour': number;
      'meetingEndTime-minute': number;
    };
    const convertedTimeString = {
      meetingStartTime: `${formDataObject['meetingStartTime-hour']
        .toString()
        .padStart(2, '0')}:${formDataObject['meetingStartTime-minute']
        .toString()
        .padStart(2, '0')}`,
      meetingEndTime: `${formDataObject['meetingEndTime-hour']
        .toString()
        .padStart(2, '0')}:${formDataObject['meetingEndTime-minute']
        .toString()
        .padStart(2, '0')}`,
    };

    createEventsMutation.mutate({
      events: selectedDates.map((date) => ({
        date: dateToFormattedString(date),
        ...convertedTimeString,
      })),
    });

    clearSelectedDates();
  };

  const handleRemoveEventsButtonClick = () => {
    removeEventsMutation.mutate({
      dates: selectedDates.map((date) => dateToFormattedString(date)),
    });
    clearSelectedDates();
  };

  if (eventsQuery.isLoading) {
    return (
      <S.Layout>
        <S.SpinnerBox>
          <Spinner />
        </S.SpinnerBox>
      </S.Layout>
    );
  }

  if (eventsQuery.isError) {
    return (
      <S.Layout>
        <S.SpinnerBox>
          <ErrorIcon />
        </S.SpinnerBox>
      </S.Layout>
    );
  }

  if (!meetingQuery.data?.data.isLoginUserMaster) {
    return (
      <S.Layout>
        <S.CalendarBox>
          <Calendar readOnly />
        </S.CalendarBox>
      </S.Layout>
    );
  }

  return (
    <S.Layout>
      <S.CalendarBox>
        <Calendar readOnly={false} />
      </S.CalendarBox>
      <S.CalendarSideSection>
        <S.CalendarControlTitle>일정 설정</S.CalendarControlTitle>
        {selectedDates.length === 0 ? (
          <S.CalendarControlHintParagraph>
            날짜를 선택하여 일정을 추가, 수정하거나 삭제하세요.
          </S.CalendarControlHintParagraph>
        ) : (
          <S.CalenderControlBox>
            <S.Form id="add-events-form" onSubmit={handleUpdateEventsSubmit}>
              <S.Label>추가/수정</S.Label>
              <S.FieldGroupBox>
                <S.FieldBox>
                  <S.Label>
                    시작 시간
                    <TimePicker
                      name="meetingStartTime"
                      min={
                        selectedDates.some(
                          (date) =>
                            date.toDateString() === currentDate.toDateString()
                        )
                          ? {
                              hour: currentDate.getHours(),
                              minute: currentDate.getMinutes(),
                            }
                          : undefined
                      }
                      max={{ hour: 23, minute: 40 }}
                      value={meetingStartTime}
                      onChange={(value) => {
                        setMeetingStartTime((prev) => ({
                          ...prev,
                          ...value,
                        }));
                      }}
                    />
                  </S.Label>
                </S.FieldBox>
                <S.FieldBox>
                  <S.Label>
                    마감 시간
                    <TimePicker
                      name="meetingEndTime"
                      min={
                        selectedDates.some(
                          (date) =>
                            date.toDateString() === currentDate.toDateString()
                        )
                          ? {
                              hour: currentDate.getHours(),
                              minute: currentDate.getMinutes() + 10,
                            }
                          : undefined
                      }
                      value={meetingEndTime}
                      onChange={(value) => {
                        setMeetingEndTime((prev) => ({
                          ...prev,
                          ...value,
                        }));
                      }}
                      disabled={!meetingStartTime}
                    />
                  </S.Label>
                </S.FieldBox>
              </S.FieldGroupBox>
              <Button
                type="submit"
                disabled={
                  meetingStartTime?.hour === undefined ||
                  meetingStartTime?.minute === undefined ||
                  meetingEndTime?.hour === undefined ||
                  meetingEndTime?.minute === undefined
                }
              >
                저장
              </Button>
            </S.Form>
            <S.Label>삭제</S.Label>
            <Button type="button" onClick={handleRemoveEventsButtonClick}>
              삭제
            </Button>
          </S.CalenderControlBox>
        )}
      </S.CalendarSideSection>
    </S.Layout>
  );
};

export default CalendarPage;
