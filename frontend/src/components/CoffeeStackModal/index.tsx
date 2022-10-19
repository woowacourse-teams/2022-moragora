import { useContext } from 'react';
import { css } from '@emotion/react';
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
  const userState = useContext(userContext) as UserContextValues;
  const {
    isLoading,
    isError,
    data: userCoffeeStatsResponse,
  } = useQuery(
    ['userCoffeeStats'],
    getUserCoffeeStatsApi(id, userState.accessToken),
    {
      onError: () => {
        alert('유저별 커피정보를 불러오는 중 에러가 발생했습니다.');
      },
    }
  );

  if (isLoading) {
    return (
      <S.Layout>
        <Spinner />
      </S.Layout>
    );
  }

  if (isError) {
    return (
      <S.Layout>
        <ErrorIcon />
        <DialogButton variant="dismiss" type="button" onClick={onDismiss}>
          닫기
        </DialogButton>
      </S.Layout>
    );
  }

  return (
    <S.Layout>
      <S.Title>It's 커피 타임~</S.Title>
      <S.StatsBox>
        {userCoffeeStatsResponse?.body.userCoffeeStats.map((data) => (
          <S.RowBox key={data.id}>
            <span>{data.nickname}</span>
            <S.CoffeeIconBox>
              <img
                src={coffeeIcon}
                alt="coffee-icon"
                css={css`
                  width: 1.5rem;
                `}
              />
              <span>X</span>
              <span>{data.coffeeCount}</span>
            </S.CoffeeIconBox>
          </S.RowBox>
        ))}
      </S.StatsBox>
      <S.Box>
        <S.ButtonBox>
          <DialogButton variant="dismiss" type="button" onClick={onDismiss}>
            안 마실래요
          </DialogButton>
          <DialogButton variant="confirm" type="button" onClick={onConfirm}>
            잘 마실게요!
          </DialogButton>
        </S.ButtonBox>
      </S.Box>
    </S.Layout>
  );
};

export default CoffeeStackModal;
