import { useContext, useState } from 'react';
import { Navigate, useNavigate, useParams } from 'react-router-dom';
import * as S from './MeetingPage.styled';
import Footer from 'components/layouts/Footer';
import Spinner from 'components/@shared/Spinner';
import ErrorIcon from 'components/@shared/ErrorIcon';
import DivideLine from 'components/@shared/DivideLine';
import ReloadButton from 'components/@shared/ReloadButton';
import UserItem from 'components/UserItem';
import ModalPortal from 'components/ModalPortal';
import CoffeeStackModal from 'components/CoffeeStackModal';
import CoffeeStackProgress from 'components/CoffeeStackProgress';
import { userContext, UserContextValues } from 'contexts/userContext';
import { postEmptyCoffeeStackApi, getMeetingData } from 'apis/meetingApis';
import useMutation from 'hooks/useMutation';
import useQuery from 'hooks/useQuery';

const MeetingPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  if (!id) {
    return <Navigate to={'/error'} />;
  }

  const { accessToken } = useContext(userContext) as UserContextValues;
  const [isModalOpened, setIsModalOpened] = useState(false);
  const [totalTardyCount, setTotalTardyCount] = useState<number>(0);
  const meetingQuery = useQuery(['meeting'], getMeetingData(id, accessToken), {
    onSuccess: (data) => {
      const totalTardyCount = data.body.users.reduce(
        (total, user) => total + user.tardyCount,
        0
      );
      setTotalTardyCount(totalTardyCount);
    },
  });

  const { mutate } = useMutation(postEmptyCoffeeStackApi, {
    onSuccess: () => {
      alert('커피 비우기에 성공했습니다.');
      meetingQuery.refetch();
    },
    onError: () => {
      alert('커피 비우기를 실패했습니다.');
    },
    onSettled: () => {
      setIsModalOpened(false);
    },
  });

  const handleEmptyButtonClick = () => {
    setIsModalOpened(true);
  };

  const handleConfirm = () => {
    if (id && accessToken) {
      mutate({ id, accessToken });
    }
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

  if (meetingQuery.isError || !id || !meetingQuery.data?.body) {
    return (
      <>
        <S.Layout>
          <S.ErrorBox>
            <ErrorIcon />
            <ReloadButton
              onClick={() => {
                meetingQuery.refetch();
              }}
            />
          </S.ErrorBox>
        </S.Layout>
        <Footer />
      </>
    );
  }

  console.log(meetingQuery.data.body);
  return (
    <>
      {isModalOpened && (
        <ModalPortal closePortal={() => setIsModalOpened(false)}>
          <CoffeeStackModal
            onConfirm={handleConfirm}
            onDismiss={() => setIsModalOpened(false)}
          />
        </ModalPortal>
      )}
      <S.Layout>
        <S.TitleSection>
          <h1>{meetingQuery.data.body.name}</h1>
        </S.TitleSection>
        <DivideLine />
        {!meetingQuery.data.body.hasUpcomingEvent && (
          <>
            <S.EmptyStateBox>
              <S.EmptyStateTitle>다가오는 일정이 없습니다.</S.EmptyStateTitle>
              <S.EmptyStateParagraph>
                다음 일정을 설정하세요.
              </S.EmptyStateParagraph>
              <S.EventCreateLink to={`/meeting/${id}/config`}>
                일정 설정하기
              </S.EventCreateLink>
            </S.EmptyStateBox>
            <DivideLine />
          </>
        )}
        <S.MeetingDetailBox>
          <S.MeetingStatusSection>
            <S.SectionTitle>출결상황</S.SectionTitle>
            <S.ProgressBox>
              <CoffeeStackProgress
                percent={
                  (totalTardyCount / meetingQuery.data.body.users.length) * 100
                }
              />
              <S.StackDetailBox>
                {meetingQuery.data.body.isMaster &&
                meetingQuery.data.body.isCoffeeTime ? (
                  <S.EmptyButton
                    variant="confirm"
                    type="button"
                    onClick={handleEmptyButtonClick}
                  >
                    비우기
                  </S.EmptyButton>
                ) : (
                  <p>
                    <span>{totalTardyCount}</span> /
                    <span>{meetingQuery.data.body.users.length}</span>
                  </p>
                )}
              </S.StackDetailBox>
            </S.ProgressBox>
          </S.MeetingStatusSection>
          <S.UserListSection>
            <S.UserListSectionHeader>
              <S.SectionTitle>출결</S.SectionTitle>
              <p>
                총 출석일: <span>{meetingQuery.data.body.attendanceCount}</span>
              </p>
            </S.UserListSectionHeader>
            <S.UserListBox>
              <S.UserList>
                {meetingQuery.data.body.users.map((user) => (
                  <UserItem
                    key={user.id}
                    meetingId={id}
                    user={user}
                    disabled={!meetingQuery.data?.body.isMaster}
                  />
                ))}
              </S.UserList>
            </S.UserListBox>
          </S.UserListSection>
        </S.MeetingDetailBox>
      </S.Layout>
      <Footer />
    </>
  );
};

export default MeetingPage;
