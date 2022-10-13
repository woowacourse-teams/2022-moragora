import * as S from './EmailConfirmModal.styled';

type EmailConfirmModalProps = {};

const EmailConfirmModal: React.FC<
  React.PropsWithChildren<EmailConfirmModalProps>
> = () => {
  return (
    <S.Layout>
      <S.Paragraph>
        <span>이메일로 전송된</span>
        <span>인증번호 6자리를 입력해주세요</span>
      </S.Paragraph>
      <S.Form>
        <S.InputBox>
          <S.NumberInput placeholder="123456" />
          <S.ExpiredTimeParagraph>04:12</S.ExpiredTimeParagraph>
        </S.InputBox>
        <S.Button>인증번호 재요청</S.Button>
      </S.Form>
    </S.Layout>
  );
};

export default EmailConfirmModal;
