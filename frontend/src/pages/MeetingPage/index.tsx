import { useParams } from 'react-router-dom';
import * as S from './MeetingPage.styled';
import Footer from 'components/layouts/Footer';
import Spinner from 'components/@shared/Spinner';
import ErrorIcon from 'components/@shared/ErrorIcon';
import { useContext } from 'react';
import DivideLine from 'components/@shared/DivideLine';
import ReloadButton from 'components/@shared/ReloadButton';
import UserItem from 'components/UserItem';
import useQuery from 'hooks/useQuery';
import { userContext, UserContextValues } from 'contexts/userContext';
import { getMeetingData } from 'utils/Apis/meetingApis';

const MeetingPage = () => {
  const { id } = useParams();
  const { accessToken } = useContext(userContext) as UserContextValues;
  const {
    data: meetingResponse,
    isLoading,
    isError,
    refetch,
  } = useQuery(['meeting'], getMeetingData(id, accessToken));

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

  if (isError || !id || !meetingResponse?.body) {
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
          <h2>{meetingResponse.body.name}</h2>
          <p>
            총 출석일: <span>{meetingResponse.body.attendanceCount}</span>
          </p>
        </S.MeetingDetailSection>
        <DivideLine />
        <S.UserListSection>
          <S.UserListBox>
            <S.UserList>
              {meetingResponse.body.users.map((user) => (
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
