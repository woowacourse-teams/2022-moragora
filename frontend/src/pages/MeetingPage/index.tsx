import React from 'react';
import * as S from './MeetingPage.styled';
import Footer from '../../components/layouts/Footer';
import Button from '../../components/@shared/Button';
import { css } from '@emotion/react';
import useFetch from '../../hooks/useFetch';
import Spinner from '../../components/@shared/Spinner';
import ErrorIcon from '../../components/@shared/ErrorIcon';

type MeetingResponseBody = {
  meetingCount: number;
};

type UsersResponseBody = {
  id: number;
  name: string;
  absentCount: number;
}[];

const submitAttendenceData = async (url: string, payload: any) => {
  return fetch(url, {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  });
};

const MeetingPage = () => {
  const meetingState = useFetch<MeetingResponseBody>('/meetings/1');
  const usersState = useFetch<UsersResponseBody>('/meetings/1/useasdfrs');

  const handleSubmit: React.FormEventHandler<HTMLFormElement> = async (e) => {
    e.preventDefault();

    const target = e.target as HTMLFormElement;
    const formData = new FormData(target);
    const formDataObject = Object.fromEntries(formData.entries());
    const payload = Object.entries(formDataObject).map(([id, value]) => ({
      id,
      isAbsent: value === 'absent',
    }));

    const response = await submitAttendenceData('/meetings/1', payload);

    if (response.ok) {
      alert('출결 마감했습니다.');

      meetingState.refetch();
      usersState.refetch();
    }
  };

  if (meetingState.loading || usersState.loading) {
    return (
      <>
        <S.Layout>
          <div
            css={css`
              flex: 1;
              display: flex;
              justify-content: center;
              align-items: center;
            `}
          >
            <Spinner />
          </div>
        </S.Layout>
        <Footer>
          <Button form="attendance-form" type="submit" disabled>
            출결 마감
          </Button>
        </Footer>
      </>
    );
  }

  if (meetingState.error || usersState.error) {
    return (
      <>
        <S.Layout>
          <div
            css={css`
              flex: 1;
              display: flex;
              justify-content: center;
              align-items: center;
            `}
          >
            <ErrorIcon />
          </div>
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
      <S.Layout>
        <S.MeetingDetailSection>
          <S.MeetingTitle>모임</S.MeetingTitle>
          <S.Paragraph>
            총 출석일 <span>{meetingState.data.meetingCount}</span>
          </S.Paragraph>
        </S.MeetingDetailSection>
        <S.UserListSection>
          <S.UserRowBox
            css={css`
              margin: 1rem 0;
            `}
          >
            <S.UserDataBox>이름</S.UserDataBox>
            <S.UserDataBox>결석일</S.UserDataBox>
            <S.UserDataBox>출석률</S.UserDataBox>
            <S.UserDataBox
              css={css`
                display: flex;
                gap: 1rem;
              `}
            >
              <label>출</label>
              <label>결</label>
            </S.UserDataBox>
          </S.UserRowBox>
          <S.DivideLine />
          <S.Form id="attendance-form" onSubmit={handleSubmit}>
            <S.UserListBox>
              {usersState.data.map((user) => (
                <S.UserRowBox key={user.id}>
                  <S.UserDataBox>{user.name}</S.UserDataBox>
                  <S.UserDataBox>{user.absentCount}</S.UserDataBox>
                  <S.UserDataBox>
                    {Math.floor(
                      ((meetingState.data.meetingCount - user.absentCount) /
                        meetingState.data.meetingCount) *
                        100
                    )}
                    %
                  </S.UserDataBox>
                  <S.UserDataBox
                    css={css`
                      display: flex;
                      gap: 1rem;
                    `}
                  >
                    <label hidden>출석</label>
                    <S.RadioButton
                      name={`${user.id}`}
                      type="radio"
                      value="attendance"
                      defaultChecked
                    />
                    <label hidden>결석</label>
                    <S.RadioButton
                      name={`${user.id}`}
                      type="radio"
                      value="absent"
                    />
                  </S.UserDataBox>
                </S.UserRowBox>
              ))}
            </S.UserListBox>
          </S.Form>
        </S.UserListSection>
      </S.Layout>
      <Footer>
        <Button form="attendance-form" type="submit">
          출결 마감
        </Button>
      </Footer>
    </>
  );
};

export default MeetingPage;
