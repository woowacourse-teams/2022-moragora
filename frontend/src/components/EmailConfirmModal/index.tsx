import { useEffect, useState } from 'react';
import * as S from './EmailConfirmModal.styled';
import { postVerifyCodeAPi } from 'apis/userApis';
import useMutation from 'hooks/useMutation';
import useTimer from 'hooks/useTimer';
import { User, UserEmailSendRequestBody } from 'types/userType';
import { getTimestampDiff } from 'utils/timeUtil';

type EmailConfirmModalProps = {
  email: User['email'];
  expiredTimestamp: number;
  onSuccess: () => void;
  onDismiss: () => void;
  refetch: (variables: UserEmailSendRequestBody) => Promise<void>;
};

const EmailConfirmModal: React.FC<
  React.PropsWithChildren<EmailConfirmModalProps>
> = ({ email, expiredTimestamp, onSuccess, onDismiss, refetch }) => {
  const [code, setCode] = useState('');
  const [isValidCode, setIsValidCode] = useState(false);
  const [isTimeOver, setIsTimeOver] = useState(false);
  const { currentTimestamp } = useTimer(0);
  const remainTime = getTimestampDiff(expiredTimestamp, currentTimestamp);

  const codeVerifyMutation = useMutation(postVerifyCodeAPi, {
    onSuccess: () => {
      alert('인증을 완료했습니다.');
      onSuccess();
    },
    onError: () => {
      alert('인증을 실패했습니다.');
    },
  });

  const handleReSendClick = () => {
    if (confirm('인증 번호를 재요청하시겠습니까?')) {
      setIsTimeOver(false);
      refetch({ email });
    }
  };

  const handleInputChange: React.ChangeEventHandler<HTMLInputElement> = ({
    target: { value },
  }) => {
    if (/^[0-9]{0,6}$/.test(value)) {
      setCode(value);
    }
  };

  const handleInputKeyDown: React.KeyboardEventHandler<HTMLInputElement> = (
    event
  ) => {
    if (['+', '-', '.', 'E', 'e'].includes(event.key)) {
      event.preventDefault();
    }
  };

  const handleSubmit: React.FormEventHandler<HTMLFormElement> = (event) => {
    event.preventDefault();

    if (isValidCode && !isTimeOver) {
      codeVerifyMutation.mutate({
        email,
        verifyCode: code,
      });
    }
  };

  useEffect(() => {
    if (expiredTimestamp - currentTimestamp <= 0) {
      setCode('');
      setIsTimeOver(true);
    }
  }, [expiredTimestamp, currentTimestamp]);

  useEffect(() => {
    if (code.length !== 6) {
      setIsValidCode(false);
      return;
    }

    setIsValidCode(true);
  }, [code]);

  return (
    <S.Layout>
      <S.Paragraph>
        이메일로 전송된
        <br />
        인증번호 6자리를 입력해주세요.
      </S.Paragraph>
      <S.Form onSubmit={handleSubmit}>
        <S.InputBox>
          <S.NumberInput
            type="number"
            autoComplete="off"
            placeholder={isTimeOver ? '인증시간 만료' : '123456'}
            disabled={isTimeOver}
            name="code"
            onChange={handleInputChange}
            onKeyDown={handleInputKeyDown}
            value={code}
            required
            autoFocus
          />
          <S.ExpiredTimeParagraph>{remainTime}</S.ExpiredTimeParagraph>
        </S.InputBox>
        {!isValidCode || isTimeOver ? (
          <S.ReSendButton onClick={handleReSendClick}>
            인증번호 재요청
          </S.ReSendButton>
        ) : (
          <S.ConfirmButton disabled={codeVerifyMutation.isLoading}>
            확인
          </S.ConfirmButton>
        )}
      </S.Form>
      <S.CloseButton onClick={onDismiss}>ⅹ</S.CloseButton>
    </S.Layout>
  );
};

export default EmailConfirmModal;
