import { useContext, useState } from 'react';
import { Navigate, useParams } from 'react-router-dom';
import * as S from './CoffeeStackPage.styled';
import Spinner from 'components/@shared/Spinner';
import ErrorIcon from 'components/@shared/ErrorIcon';
import DivideLine from 'components/@shared/DivideLine';
import ReloadButton from 'components/@shared/ReloadButton';
import CoffeeStackItem from 'components/CoffeeStackItem';
import ModalPortal from 'components/ModalPortal';
import CoffeeStackModal from 'components/CoffeeStackModal';
import CoffeeStackProgress from 'components/CoffeeStackProgress';
import { userContext, UserContextValues } from 'contexts/userContext';
import { postEmptyCoffeeStackApi, getMeetingData } from 'apis/meetingApis';
import useMutation from 'hooks/useMutation';
import useQuery from 'hooks/useQuery';
import { getUpcomingEvent } from 'apis/eventApis';
import { NOT_FOUND_STATUS_CODE } from 'consts';

const CoffeeStackPage = () => {
  const { id } = useParams();

  if (!id) {
    return <Navigate to={'/error'} />;
  }

  const { accessToken } = useContext(userContext) as UserContextValues;
  const [isModalOpened, setIsModalOpened] = useState(false);
  const [totalTardyCount, setTotalTardyCount] = useState<number>(0);
  const [upcomingEventNotExist, setUpcomingEventNotExist] = useState(false);

  const meetingQuery = useQuery(['meeting'], getMeetingData(id, accessToken), {
    onSuccess: ({ body: { users } }) => {
      const totalTardyCount = users.reduce(
        (total, user) => total + user.tardyCount,
        0
      );
      setTotalTardyCount(totalTardyCount);
    },
  });

  const upcomingEventQuery = useQuery(
    ['upcomingEvent'],
    getUpcomingEvent(id, accessToken),
    {
      enabled: meetingQuery.isSuccess,
      onError: (error) => {
        setUpcomingEventNotExist(
          parseInt(error.message.split(':')[0]) === NOT_FOUND_STATUS_CODE
        );
      },
    }
  );

  const emptyCoffeeStackMutation = useMutation(postEmptyCoffeeStackApi, {
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
    emptyCoffeeStackMutation.mutate({ id, accessToken });
  };

  if (meetingQuery.isLoading || upcomingEventQuery.isLoading) {
    return (
      <>
        <S.Layout>
          <S.SpinnerBox>
            <Spinner />
          </S.SpinnerBox>
        </S.Layout>
      </>
    );
  }

  if (!id || meetingQuery.isError || !meetingQuery.data?.body) {
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
      </>
    );
  }

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
        {upcomingEventNotExist && meetingQuery.data.body.isLoginUserMaster && (
          <>
            <S.EmptyStateBox>
              <S.EmptyStateTitle>다가오는 일정이 없습니다.</S.EmptyStateTitle>
              <S.EmptyStateParagraph>
                다음 일정을 설정하세요.
              </S.EmptyStateParagraph>
              <S.EventCreateLink to={`/meeting/${id}/event`}>
                일정 설정하기
              </S.EventCreateLink>
            </S.EmptyStateBox>
            <DivideLine />
          </>
        )}
        <S.TitleSection>
          <h1>{meetingQuery.data.body.name}</h1>
        </S.TitleSection>
        <DivideLine />
        <S.MeetingDetailBox>
          <S.MeetingStatusSection>
            <S.ProgressBox>
              <CoffeeStackProgress
                percent={
                  (totalTardyCount / meetingQuery.data.body.users.length) * 100
                }
              />
              <S.StackDetailBox>
                {meetingQuery.data.body.isLoginUserMaster &&
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
              <S.SectionTitle>커피 스택 현황</S.SectionTitle>
              <p>
                총 출석일:{' '}
                <span>{meetingQuery.data.body.attendanceEventCount}</span>
              </p>
            </S.UserListSectionHeader>
            <S.UserListBox>
              <S.UserList>
                {meetingQuery.data.body.users.map((user) => (
                  <CoffeeStackItem
                    key={user.id}
                    name={user.nickname}
                    tardyCount={user.tardyCount}
                    isMaster={user.isMaster}
                  />
                ))}
              </S.UserList>
            </S.UserListBox>
          </S.UserListSection>
        </S.MeetingDetailBox>
      </S.Layout>
    </>
  );
};

export default CoffeeStackPage;
