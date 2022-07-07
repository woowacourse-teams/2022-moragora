import React from 'react';
import * as S from './MeetingPage.styled';
import Footer from '../../components/layouts/Footer';
import Button from '../../components/@shared/Button';

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
          <form id="attendance-form" onSubmit={handleSubmit}>
            <S.Table>
              <thead>
                <tr>
                  <th>이름</th>
                  <th>결석일</th>
                  <th>출석률</th>
                  <th>출결</th>
                </tr>
              </thead>
              <tbody>
                {users.map((user) => (
                  <S.TableRow key={user.id}>
                    <S.TabelData>{user.name}</S.TabelData>
                    <S.TabelData>{user.absentCount}</S.TabelData>
                    <S.TabelData>
                      {Math.floor(
                        ((meeting.meetingCount - user.absentCount) /
                          meeting.meetingCount) *
                          100
                      )}
                      %
                    </S.TabelData>
                    <S.TabelData>
                      <label>
                        출
                        <input
                          name={`${user.id}`}
                          type="radio"
                          value="attendance"
                          defaultChecked
                        />
                      </label>
                      <label>
                        결
                        <input
                          name={`${user.id}`}
                          type="radio"
                          value="absent"
                        />
                      </label>
                    </S.TabelData>
                  </S.TableRow>
                ))}
              </tbody>
            </S.Table>
          </form>
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
