import React from 'react';
import * as S from './MeetingPage.styled';
import Footer from '../../components/layouts/Footer';
import Button from '../../components/@shared/Button';
import { css } from '@emotion/react';

const meeting = {
  meetingCount: 7,
};
const users = [
  {
    id: 1,
    name: 'fildz',
    absentCount: 1,
  },
  {
    id: 2,
    name: 'woody',
    absentCount: 1,
  },
  {
    id: 3,
    name: 'badd',
    absentCount: 1,
  },
  {
    id: 4,
    name: 'forky',
    absentCount: 1,
  },
  {
    id: 5,
    name: 'sun',
    absentCount: 1,
  },
  {
    id: 6,
    name: 'kun',
    absentCount: 1,
  },
  {
    id: 7,
    name: 'aspy',
    absentCount: 1,
  },
  {
    id: 8,
    name: 'fildz',
    absentCount: 1,
  },
  {
    id: 9,
    name: 'woody',
    absentCount: 1,
  },
  {
    id: 10,
    name: 'badd',
    absentCount: 1,
  },
  {
    id: 11,
    name: 'forky',
    absentCount: 1,
  },
  {
    id: 12,
    name: 'sun',
    absentCount: 1,
  },
  {
    id: 13,
    name: 'kun',
    absentCount: 1,
  },
  {
    id: 14,
    name: 'aspy',
    absentCount: 1,
  },
  {
    id: 15,
    name: 'fildz',
    absentCount: 1,
  },
  {
    id: 16,
    name: 'woody',
    absentCount: 1,
  },
  {
    id: 17,
    name: 'badd',
    absentCount: 1,
  },
  {
    id: 18,
    name: 'forky',
    absentCount: 1,
  },
  {
    id: 19,
    name: 'sun',
    absentCount: 1,
  },
  {
    id: 20,
    name: 'kun',
    absentCount: 1,
  },
  {
    id: 21,
    name: 'aspy',
    absentCount: 1,
  },
];

const MeetingPage = () => {
  const handleSubmit: React.FormEventHandler<HTMLFormElement> = (e) => {
    e.preventDefault();

    const target = e.target as HTMLFormElement;
    const formData = new FormData(target);
    const formDataObject = Object.fromEntries(formData.entries());
    const payload = Object.entries(formDataObject).map(([id, value]) => ({
      id,
      isAbsent: value === 'absent',
    }));

    console.log(payload);
  };

  return (
    <>
      <S.Layout>
        <S.MeetingDetailSection>
          <S.MeetingTitle>모임</S.MeetingTitle>
          <S.Paragraph>
            총 출석일 <span>{meeting.meetingCount}</span>
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
              {users.map((user) => (
                <S.UserRowBox key={user.id}>
                  <S.UserDataBox>{user.name}</S.UserDataBox>
                  <S.UserDataBox>{user.absentCount}</S.UserDataBox>
                  <S.UserDataBox>
                    {Math.floor(
                      ((meeting.meetingCount - user.absentCount) /
                        meeting.meetingCount) *
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
