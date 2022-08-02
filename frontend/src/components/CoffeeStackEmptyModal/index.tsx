import * as S from './CoffeeStackEmptyModal.styled';
import coffeeIcon from 'assets/beverage_icon.svg';
import DialogButton from 'components/@shared/DialogButton';

type CoffeeStackEmptyModalProps = {
  onConfirm: React.MouseEventHandler;
  onDismiss?: React.MouseEventHandler;
};

const CoffeeStackEmptyModal = ({
  onDismiss,
  onConfirm,
}: CoffeeStackEmptyModalProps) => {
  return (
    <S.Layout>
      <S.Header>It's 커피 파티 타임~</S.Header>
      <img src={coffeeIcon} width="220rem" />
      <S.ButtonBox>
        <DialogButton variant="confirm" type="button" onClick={onDismiss}>
          안 마실래요
        </DialogButton>
        <DialogButton variant="confirm" type="button" onClick={onConfirm}>
          잘마실게요!
        </DialogButton>
      </S.ButtonBox>
    </S.Layout>
  );
};

export default CoffeeStackEmptyModal;
