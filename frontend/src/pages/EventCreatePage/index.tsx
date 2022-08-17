import { useContext } from 'react';
import { Navigate, useNavigate, useParams } from 'react-router-dom';
import { css } from '@emotion/react';
import * as S from './EventCreatePage.styled';
import Footer from 'components/layouts/Footer';
import Calendar from 'components/@shared/Calendar';
import Button from 'components/@shared/Button';
import Input from 'components/@shared/Input';
import DialogButton from 'components/@shared/DialogButton';
import Spinner from 'components/@shared/Spinner';
import NotFoundPage from 'pages/NotFoundPage';
import { userContext, UserContextValues } from 'contexts/userContext';
import { CalendarContext } from 'contexts/calendarContext';
import useForm from 'hooks/useForm';
import useMutation from 'hooks/useMutation';
import useQuery from 'hooks/useQuery';
import { getMeetingData } from 'apis/meetingApis';
import { createEventsApi } from 'apis/eventApis';
import { dateToFormattedString } from 'utils/timeUtil';

const EventCreatePage = () => {
  const { id: meetingId } = useParams();

  if (!meetingId) {
    return <Navigate to="/error" />;
  }

  const user = useContext(userContext) as UserContextValues;
  const navigate = useNavigate();
  const { events, selectedDates, clearSelectedDates, addEvents, removeEvents } =
    useContext(CalendarContext);
  const { values, errors, onSubmit, register } = useForm();
  const meetingQuery = useQuery(
    ['meeting', meetingId],
    getMeetingData(meetingId, user.accessToken)
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

  const handleAddEventsSubmit = () => {
    addEvents(
      selectedDates.map((date) => ({
        date: dateToFormattedString(date),
        entranceTime: values['entranceTime'] as string,
        leaveTime: values['leaveTime'] as string,
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

  if (meetingQuery.isLoading) {
    return (
      <>
        <S.Layout>
          <S.SpinnerBox>
            <Spinner />
          </S.SpinnerBox>
        </S.Layout>
        <Footer />
      </>
    );
  }

  if (!meetingQuery.data) {
    return <NotFoundPage />;
  }

  if (!meetingQuery.data.body.isLoginUserMaster) {
    return <Navigate to="/" />;
  }

  return (
    <>
      <S.Layout>
        <S.CalendarBox>
          <Calendar />
        </S.CalendarBox>
        <S.Form
          id="add-events-form"
          {...onSubmit(handleAddEventsSubmit)}
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
            완료
          </Button>
        </S.ButtonBox>
      </S.Layout>
    </>
  );
};

export default EventCreatePage;
