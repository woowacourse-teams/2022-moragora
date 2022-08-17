import { useContext, useState } from 'react';
import { Navigate, useOutletContext, useParams } from 'react-router-dom';
import * as S from './CoffeeStackPage.styled';
import DivideLine from 'components/@shared/DivideLine';
import CoffeeStackItem from 'components/CoffeeStackItem';
import ModalPortal from 'components/ModalPortal';
import CoffeeStackModal from 'components/CoffeeStackModal';
import CoffeeStackProgress from 'components/CoffeeStackProgress';
import { userContext, UserContextValues } from 'contexts/userContext';
import { getMeetingData, postEmptyCoffeeStackApi } from 'apis/meetingApis';
import useMutation from 'hooks/useMutation';
import { getUpcomingEventApi } from 'apis/eventApis';
import { QueryState } from 'types/queryType';

const CoffeeStackPage = () => {
  const { id } = useParams();
  const { accessToken } = useContext(userContext) as UserContextValues;

  if (!id) {
    return <Navigate to={'/error'} />;
  }

  const [isModalOpened, setIsModalOpened] = useState(false);

  const { meetingQuery, totalTardyCount, upcomingEventNotExist } =
    useOutletContext<{
      meetingQuery: QueryState<typeof getMeetingData>;
      upcomingEventQuery: QueryState<typeof getUpcomingEventApi>;
      totalTardyCount: number;
      upcomingEventNotExist: boolean;
    }>();

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
        {upcomingEventNotExist && meetingQuery.data?.body.isLoginUserMaster && (
          <>
            <S.EmptyStateBox>
              <S.EmptyStateTitle>다가오는 일정이 없습니다.</S.EmptyStateTitle>
              <S.EmptyStateParagraph>
                다음 일정을 설정하세요.
              </S.EmptyStateParagraph>
              <S.EventCreateLink to={`/meeting/${id}/calendar`}>
                일정 설정하기
              </S.EventCreateLink>
            </S.EmptyStateBox>
            <DivideLine />
          </>
        )}
        <S.TitleSection>
          <h1>{meetingQuery.data?.body.name}</h1>
        </S.TitleSection>
        <DivideLine />
        <S.MeetingDetailBox>
          <S.MeetingStatusSection>
            <S.ProgressBox>
              <CoffeeStackProgress
                percent={
                  (totalTardyCount /
                    (meetingQuery.data?.body.users.length ?? 0)) *
                  100
                }
              />
              <S.StackDetailBox>
                {meetingQuery.data?.body.isLoginUserMaster &&
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
                    <span>{meetingQuery.data?.body.users.length}</span>
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
                <span>{meetingQuery.data?.body.attendanceEventCount}</span>
              </p>
            </S.UserListSectionHeader>
            <S.UserListBox>
              <S.UserList>
                {meetingQuery.data?.body.users.map((user) => (
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
