import * as S from './CoffeeStackEmptyModal.styled';
import coffeeIcon from 'assets/coffee-cup.svg';
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
      <S.Header>It's 커피 타임~</S.Header>
      <img src={coffeeIcon} width="220rem" />
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

export default CoffeeStackEmptyModal;
