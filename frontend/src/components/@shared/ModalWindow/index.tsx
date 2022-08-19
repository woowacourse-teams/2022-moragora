import React from 'react';
import DialogButton from '../DialogButton';
import * as S from './ModalWindow.styled';

type ModalWindowProps = {
  message: string;
  onConfirm: React.MouseEventHandler;
  onDismiss?: React.MouseEventHandler;
};

const ModalWindow: React.FC<ModalWindowProps> = ({
  message,
  onConfirm,
  onDismiss,
}) => {
  return (
    <S.Layout>
      <S.Title>{message}</S.Title>
      <S.Box>
        <S.ButtonBox>
          <DialogButton variant="dismiss" type="button" onClick={onDismiss}>
            취소
          </DialogButton>
          <DialogButton variant="confirm" type="button" onClick={onConfirm}>
            확인
          </DialogButton>
        </S.ButtonBox>
      </S.Box>
    </S.Layout>
  );
};

export default ModalWindow;
