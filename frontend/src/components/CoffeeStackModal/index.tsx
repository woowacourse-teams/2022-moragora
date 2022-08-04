import { useContext } from 'react';
import { useParams } from 'react-router-dom';
import * as S from './CoffeeStackModal.styled';
import useQuery from 'hooks/useQuery';
import DialogButton from 'components/@shared/DialogButton';
import Spinner from 'components/@shared/Spinner';
import ErrorIcon from 'components/@shared/ErrorIcon';
import { userContext, UserContextValues } from 'contexts/userContext';
import { getUserCoffeeStatsApi } from 'apis/userApis';
import coffeeIcon from 'assets/coffee.svg';

type CoffeeStackModalProps = {
  onConfirm: React.MouseEventHandler;
  onDismiss: React.MouseEventHandler;
};

const CoffeeStackModal = ({ onDismiss, onConfirm }: CoffeeStackModalProps) => {
  const { id } = useParams();
  const { accessToken } = useContext(userContext) as UserContextValues;
  const {
    isLoading,
    isError,
    data: userCoffeeStatsResponse,
  } = useQuery(['userCoffeeStats'], getUserCoffeeStatsApi(id, accessToken), {
    onError: () => {
      alert('유저별 커피정보를 불러오는 중 에러가 발생했습니다.');
    },
  });

  if (isLoading) {
    return (
      <S.Layout>
        <Spinner />
      </S.Layout>
    );
  }

  if (isError || !userCoffeeStatsResponse?.body) {
    return (
      <S.Layout>
        <ErrorIcon />
        <S.CloseButton variant="confirm" type="button" onClick={onDismiss}>
          닫기
        </S.CloseButton>
      </S.Layout>
    );
  }

  return (
    <S.Layout>
      <S.Header>It's 커피 타임~</S.Header>
      <S.StatsBox>
        {userCoffeeStatsResponse.body.userCoffeeStats.map((data) => (
          <S.RowBox>
            <span>{data.nickname}</span>
            <S.CoffeeIconBox>
              <img src={coffeeIcon} alt="coffee-icon" width={40} />
              <span>X</span>
              <span>{data.coffeeCount}</span>
            </S.CoffeeIconBox>
          </S.RowBox>
        ))}
      </S.StatsBox>
      <S.ButtonBox>
        <S.dismissButton variant="dismiss" type="button" onClick={onDismiss}>
          안 마실래요
        </S.dismissButton>
        <DialogButton variant="confirm" type="button" onClick={onConfirm}>
          잘마실게요!
        </DialogButton>
      </S.ButtonBox>
    </S.Layout>
  );
};

export default CoffeeStackModal;
