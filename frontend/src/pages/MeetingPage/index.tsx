import React, { useEffect, useState } from 'react';
import * as S from './MeetingPage.styled';
import Footer from '../../components/layouts/Footer';
import Button from '../../components/@shared/Button';
import useFetch from '../../hooks/useFetch';
import Spinner from '../../components/@shared/Spinner';
import ErrorIcon from '../../components/@shared/ErrorIcon';
import ModalPortal from '../../components/ModalPortal';
import ModalWindow from '../../components/@shared/ModalWindow';
import DivideLine from '../../components/@shared/DivideLine';
import ReloadButton from '../../components/@shared/ReloadButton';
import useForm from '../../hooks/useForm';
import { useParams } from 'react-router-dom';
import UserItem from '../../components/UserItem';

type User = {
  id: number;
  email: string;
  password: string;
  nickname: string;
  accessToken: null | string;
  tardyCount: number;
  attendanceStatus: string;
};

type MeetingResponseBody = {
  id: number;
  name: string;
  startDate: string;
  endDate: string;
  entranceTime: string;
  leaveTime: string;
  users: Omit<User, 'accessToken'>[];
  attendanceCount: number;
};

type FormDataObject = { [k: string]: FormDataEntryValue };

type MeetingStateDataType = {
  data: MeetingResponseBody;
  loading: boolean;
  error: any;
  fetchingCount: number;
  refetch: () => void;
};

const submitAttendanceData = async (url: string, payload: any) => {
  return fetch(url, {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  });
};

const userAttendanceFetch = async (url: string, payload: any) => {
  return fetch(url, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  });
};

const MeetingPage = () => {
  const { id } = useParams();
  const meetingState = useFetch<MeetingResponseBody>(`/meetings/${id}`);
  const [modalOpened, setModalOpened] = useState(false);
  const [formData, setFormData] = useState<FormDataObject>();
  const { isSubmitting, onSubmit, register } = useForm();

  const handleChecked = async (
    user: Omit<User, 'accessToken'>,
    checked: boolean
  ) => {
    const targetMeeting = meetingState.data.users.find(
      (userData) => userData.id === user.id
    );
    targetMeeting.attendanceStatus = checked ? 'present' : 'tardy';

    const response = await userAttendanceFetch(
      `/meetings/${id}/users/${user.id}`,
      {
        attendanceStatus: checked ? 'present' : 'tardy',
      }
    );

    meetingState.refetch();
  };

  const handleOpen = () => {
    setModalOpened(true);
  };

  const handleClose = () => {
    setModalOpened(false);
  };

  const handleSubmit: React.FormEventHandler<HTMLFormElement> = (e) => {
    const target = e.target as HTMLFormElement;
    const formData = new FormData(target);
    const formDataObject = Object.fromEntries(formData.entries());

    setFormData(formDataObject);

    handleOpen();
  };

  const handleConfirm = async () => {
    const payload = Object.entries(formData).map(([id, value]) => ({
      id,
      isAbsent: value === 'absent',
    }));

    const response = await submitAttendanceData('/meetings/1', payload);

    if (response.ok) {
      alert('출결 마감했습니다.');
      handleClose();

      meetingState.refetch();
    }
  };

  if (meetingState.loading) {
    return (
      <>
        <S.Layout>
          <S.SpinnerBox>
            <Spinner />
          </S.SpinnerBox>
        </S.Layout>
        <Footer>
          <Button form="attendance-form" type="submit" disabled>
            출결 마감
          </Button>
        </Footer>
      </>
    );
  }

  if (meetingState.error) {
    return (
      <>
        <S.Layout>
          <S.ErrorBox>
            <ErrorIcon />
            <ReloadButton
              onClick={() => {
                meetingState.refetch();
              }}
            />
          </S.ErrorBox>
        </S.Layout>
        <Footer>
          <Button form="attendance-form" type="submit" disabled>
            출결 마감
          </Button>
        </Footer>
      </>
    );
  }

  return (
    <>
      {modalOpened && (
        <ModalPortal closePortal={handleClose}>
          <ModalWindow
            message="마감하시겠습니까?"
            onConfirm={handleConfirm}
            onDismiss={handleClose}
          />
        </ModalPortal>
      )}
      <S.Layout>
        <S.MeetingDetailSection>
          <h2>{meetingState.data.name}</h2>
          <p>
            총 출석일: <span>{meetingState.data.attendanceCount}</span>
          </p>
        </S.MeetingDetailSection>
        <DivideLine />
        <S.UserListSection>
          <S.Form id="attendance-form" {...onSubmit(handleSubmit)}>
            <S.UserList>
              {meetingState.data.users.map((user) => (
                <UserItem
                  key={user.id}
                  user={user}
                  handleChecked={handleChecked}
                />
              ))}
            </S.UserList>
          </S.Form>
        </S.UserListSection>
      </S.Layout>
      <Footer />
    </>
  );
};

export default MeetingPage;
