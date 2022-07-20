import { useParams } from 'react-router-dom';
import * as S from './MeetingPage.styled';
import Footer from 'components/layouts/Footer';
import Spinner from 'components/@shared/Spinner';
import ErrorIcon from 'components/@shared/ErrorIcon';
import DivideLine from 'components/@shared/DivideLine';
import ReloadButton from 'components/@shared/ReloadButton';
import UserItem from 'components/UserItem';
import useFetch from 'hooks/useFetch';
import { MeetingResponseBody } from 'types/meetingType';

const MeetingPage = () => {
  const { id } = useParams();
  const {
    data: meetingState,
    loading,
    error,
    refetch,
  } = useFetch<MeetingResponseBody>(`/meetings/${id}`);

  if (loading) {
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

  if (error || !id || !meetingState) {
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
