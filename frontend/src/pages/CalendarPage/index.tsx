import { useContext } from 'react';
import {
  Navigate,
  useNavigate,
  useOutletContext,
  useParams,
} from 'react-router-dom';
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
  getEventsApi,
  getUpcomingEventApi,
} from 'apis/eventApis';
import { getMeetingData } from 'apis/meetingApis';
import Spinner from 'components/@shared/Spinner';
import { QueryState } from 'types/queryType';

const CalendarPage = () => {
  const { id: meetingId } = useParams();
  const navigate = useNavigate();
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
    {}
  );

  const { mutate } = useMutation(
    createEventsApi(meetingId, user?.accessToken),
    {
      onSuccess: () => {
        alert('일정을 생성했습니다.');
        navigate('/');
      },
      onError: (error) => {
        alert(error.message);
      },
    }
  );

  const {
    events,
    selectedDates,
    clearSelectedDates,
    updateEvents,
    removeEvents,
  } = useContext(CalendarContext);
  const { values, errors, onSubmit, register } = useForm();

  const handleupdateEventsSubmit = () => {
    updateEvents(
      selectedDates.map((date) => ({
        date: dateToFormattedString(date),
        meetingStartTime: values['entranceTime'] as string,
        meetingEndTime: values['leaveTime'] as string,
      }))
    );
    clearSelectedDates();
  };

  const handleRemoveEventsButtonClick = () => {
    removeEvents(selectedDates);
    clearSelectedDates();
  };

  const handleClickSaveEventsButtonClick = () => {
    mutate(events);
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

  return (
    <S.Layout>
      <S.CalendarBox>
        <Calendar
          savedEvents={eventsQuery.data?.body.events}
          readOnly={!meetingQuery.data?.body.isLoginUserMaster}
        />
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
                    {...register('entranceTime', {
                      onClick: (e) => {
                        const currentTarget =
                          e.currentTarget as HTMLInputElement & {
                            showPicker: () => void;
                          };

                        currentTarget.showPicker();
                      },
                      required: true,
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
                    {...register('leaveTime', {
                      onClick: (e) => {
                        const currentTarget =
                          e.currentTarget as HTMLInputElement & {
                            showPicker: () => void;
                          };

                        currentTarget.showPicker();
                      },
                      required: true,
                      watch: true,
                    })}
                    disabled={errors['entranceTime'] !== ''}
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
                  !values['entranceTime'] ||
                  !values['leaveTime'] ||
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
