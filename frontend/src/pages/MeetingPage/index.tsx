import React, { useState } from 'react';
import * as S from './MeetingPage.styled';
import Footer from '../../components/layouts/Footer';
import Button from '../../components/@shared/Button';
import { css } from '@emotion/react';
import useFetch from '../../hooks/useFetch';
import Spinner from '../../components/@shared/Spinner';
import ErrorIcon from '../../components/@shared/ErrorIcon';
import ModalPortal from '../../components/ModalPortal';
import ModalWindow from '../../components/@shared/ModalWindow';

type MeetingResponseBody = {
  meetingCount: number;
};

type UsersResponseBody = {
  id: number;
  name: string;
  absentCount: number;
}[];

type FormDataObject = { [k: string]: FormDataEntryValue };

const submitAttendenceData = async (url: string, payload: any) => {
  return fetch('http://localhost:8080' + url, {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  });
};

const MeetingPage = () => {
  const meetingState = useFetch<MeetingResponseBody>('/meetings/1');
  const usersState = useFetch<UsersResponseBody>('/meetings/1/users');
  const [modalOpened, setModalOpened] = useState(false);
  const [formData, setFormData] = useState<FormDataObject>();

  const handleOpen = () => {
    setModalOpened(true);
  };

  const handleClose = () => {
    setModalOpened(false);
  };

  const handleSubmit: React.FormEventHandler<HTMLFormElement> = (e) => {
    e.preventDefault();

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

    const response = await submitAttendenceData('/meetings/1', payload);

    if (response.ok) {
      alert('출결 마감했습니다.');
      handleClose();

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
              flex-direction: column;
              justify-content: center;
              align-items: center;
              gap: 2rem;
            `}
          >
            <ErrorIcon />
            <button
              type="button"
              onClick={() => {
                meetingState.refetch();
                usersState.refetch();
              }}
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                css={css`
                  border: 2px solid lightgrey;
                  padding: 1rem;
                  border-radius: 50%;
                  width: 2rem;
                  height: 2rem;

                  :hover {
                    color: lightgrey;
                  }
                `}
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
                strokeWidth={2}
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"
                />
              </svg>
            </button>
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
          <S.MeetingTitle>모라고라팀</S.MeetingTitle>
          <S.Paragraph>
            총 출석일: <span>{meetingState.data.meetingCount}</span>
          </S.Paragraph>
        </S.MeetingDetailSection>
        <S.UserListSection>
          <S.UserRowBox
            css={css`
              margin: 1rem 0;
            `}
          >
            <S.UserDataBox>이름</S.UserDataBox>
            <S.UserDataBox>지각스택</S.UserDataBox>
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
                    {meetingState.data.meetingCount === 0
                      ? '-'
                      : Math.floor(
                          ((meetingState.data.meetingCount - user.absentCount) /
                            meetingState.data.meetingCount) *
                            100
                        ) + '%'}
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
