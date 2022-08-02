import { useContext, useState } from 'react';
import { useParams } from 'react-router-dom';
import * as S from './MeetingPage.styled';
import Footer from 'components/layouts/Footer';
import Spinner from 'components/@shared/Spinner';
import ErrorIcon from 'components/@shared/ErrorIcon';
import DivideLine from 'components/@shared/DivideLine';
import ReloadButton from 'components/@shared/ReloadButton';
import UserItem from 'components/UserItem';
import ModalPortal from 'components/ModalPortal';
import CoffeeStackEmptyModal from 'components/CoffeeStackEmptyModal';
import { userContext, UserContextValues } from 'contexts/userContext';
import useMutation from 'hooks/useMutation';
import useQuery from 'hooks/useQuery';
import { emptyCoffeeStackApi, getMeetingData } from 'apis/meetingApis';
import CoffeeStackProgress from 'components/CoffeeStackProgress';

const MeetingPage = () => {
  const { id } = useParams();
  const [modalOpened, setModalOpened] = useState(false);
  const { accessToken } = useContext(userContext) as UserContextValues;
  const {
    data: meetingResponse,
    isLoading,
    isError,
    refetch: getMeetingRefetch,
  } = useQuery(['meeting'], getMeetingData(id, accessToken));

  const { mutate } = useMutation(emptyCoffeeStackApi, {
    onSuccess: () => {
      alert('커피 비우기에 성공했습니다.');
      getMeetingRefetch();
    },
    onError: () => {
      alert('커피 비우기 실패');
    },
    onSettled: () => {
      setModalOpened(false);
    },
  });

  const handleEmptyButtonClick = () => {
    setModalOpened(true);
  };

  const handleConfirm = () => {
    if (id && accessToken) {
      mutate({ id, accessToken });
    }
  };

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
                getMeetingRefetch();
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
      {modalOpened && (
        <ModalPortal closePortal={() => setModalOpened(false)}>
          <CoffeeStackEmptyModal
            onConfirm={handleConfirm}
            onDismiss={() => setModalOpened(false)}
          />
        </ModalPortal>
      )}
      <S.Layout>
        <S.TitleSection>
          <h1>{meetingResponse?.body.name}</h1>
        </S.TitleSection>
        <DivideLine />
        <S.MeetingDetailSection>
          <S.SectionTitle>출결상황</S.SectionTitle>
          <S.ProgressBox>
            <CoffeeStackProgress size={200} percent={100} />
            <S.StackDetailBox>
              {meetingResponse?.body.isMaster &&
              meetingResponse?.body.isCoffeeTime ? (
                <S.EmptyButton
                  variant="confirm"
                  type="button"
                  onClick={handleEmptyButtonClick}
                >
                  비우기
                </S.EmptyButton>
              ) : (
                <p>
                  <span>
                    {meetingResponse?.body.users.reduce(
                      (total, user) => total + user.tardyCount,
                      0
                    )}
                  </span>{' '}
                  / <span>{meetingResponse?.body.users.length}</span>
                </p>
              )}
            </S.StackDetailBox>
          </S.ProgressBox>
        </S.MeetingDetailSection>
        <S.UserListSection>
          <S.UserListSectionHeader>
            <S.SectionTitle>출결</S.SectionTitle>
            <p>
              총 출석일: <span>{meetingResponse?.body.attendanceCount}</span>
            </p>
          </S.UserListSectionHeader>
          <S.UserListBox>
            <S.UserList>
              {meetingResponse.body.users.map((user) => (
                <UserItem
                  key={user.id}
                  meetingId={id}
                  user={user}
                  disabled={!meetingResponse?.body.isMaster}
                />
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
