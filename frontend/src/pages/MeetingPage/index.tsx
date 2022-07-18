import React from 'react';
import * as S from './MeetingPage.styled';
import Footer from '../../components/layouts/Footer';
import useFetch from '../../hooks/useFetch';
import Spinner from '../../components/@shared/Spinner';
import ErrorIcon from '../../components/@shared/ErrorIcon';
import DivideLine from '../../components/@shared/DivideLine';
import ReloadButton from '../../components/@shared/ReloadButton';
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
  const {
    data: meetingState,
    loading: isLoading,
    error: isError,
    refetch,
  } = useFetch<MeetingResponseBody>(`/meetings/${id}`);

  const handleChecked = async (
    user: Omit<User, 'accessToken'>,
    checked: boolean
  ) => {
    const response = await userAttendanceFetch(
      `/meetings/${id}/users/${user.id}`,
      {
        attendanceStatus: checked ? 'present' : 'tardy',
      }
    );

    if (!response.ok) {
      alert('출석체크에 실패했습니다.');
    }

    refetch();
  };

  if (isLoading) {
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

  if (isError) {
    return (
      <>
        <S.Layout>
          <S.ErrorBox>
            <ErrorIcon />
            <ReloadButton
              onClick={() => {
                refetch();
              }}
            />
          </S.ErrorBox>
        </S.Layout>
        <Footer />
      </>
    );
  }

  return (
    <>
      <S.Layout>
        <S.MeetingDetailSection>
          <h2>{meetingState.name}</h2>
          <p>
            총 출석일: <span>{meetingState.attendanceCount}</span>
          </p>
        </S.MeetingDetailSection>
        <DivideLine />
        <S.UserListSection>
          <S.UserListBox>
            <S.UserList>
              {meetingState.users.map((user) => (
                <UserItem
                  key={user.id}
                  user={user}
                  handleChecked={handleChecked}
                />
              ))}
            </S.UserList>
          </S.UserListBox>
        </S.UserListSection>
      </S.Layout>
      <Footer />
    </>
  );
};

export default MeetingPage;
