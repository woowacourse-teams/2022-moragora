import React from 'react';
import DialogButton from '../DialogButton';
import * as S from './ModalWindow.styled';

const ModalWindow = () => {
  return (
    <S.Layout>
      <S.Title>정말 삭제하시겠습니까?</S.Title>
      <S.Box>
        <S.ButtonBox>
          <DialogButton variant="dismiss" type="button">
            취소
          </DialogButton>
          <DialogButton variant="confirm" type="button">
            확인
          </DialogButton>
        </S.ButtonBox>
      </S.Box>
    </S.Layout>
  );
};

export default ModalWindow;
