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
  attendanceStatus: 'present' | 'tardy';
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

const MeetingPage = () => {
  const { id } = useParams();
  const {
    data: meetingState,
    loading: isLoading,
    error: isError,
    refetch,
  } = useFetch<MeetingResponseBody>(`/meetings/${id}`);

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
              {meetingState &&
                meetingState.users.map((user) => (
                  <UserItem key={user.id} meetingId={id} user={user} />
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
