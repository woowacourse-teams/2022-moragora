import * as S from './EmailConfirmModal.styled';
import { postVerifyCodeAPi } from 'apis/userApis';
import useForm from 'hooks/useForm';
import useMutation from 'hooks/useMutation';
import useTimer from 'hooks/useTimer';
import { User, UserEmailSendRequestBody } from 'types/userType';
import { getTimestampDiff } from 'utils/timeUtil';
import { useEffect, useState } from 'react';

type EmailConfirmModalProps = {
  email: User['email'];
  expiredTimestamp: number;
  onSuccess: () => void;
  onDismiss: () => void;
  refetch: (variables: UserEmailSendRequestBody) => Promise<void>;
};

const EmailConfirmModal: React.FC<
  React.PropsWithChildren<EmailConfirmModalProps>
> = ({ email, expiredTimestamp, onSuccess, onDismiss, refetch: refetch }) => {
  const { values, errors, isSubmitting, onSubmit, register } = useForm();
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
    alert('인증 번호를 재요청했습니다.');
    setIsTimeOver(false);
    refetch({ email });
  };

  const handleSubmit = () => {
    if (!errors['code'] && !isTimeOver) {
      codeVerifyMutation.mutate({
        email,
        verifyCode: values['code'] as string,
      });
    }
  };

  useEffect(() => {
    if (expiredTimestamp - currentTimestamp <= 0) {
      setIsTimeOver(true);
    }
  }, [expiredTimestamp, currentTimestamp]);

  return (
    <S.Layout>
      <S.Paragraph>
        <span>이메일로 전송된</span>
        <span>인증번호 6자리를 입력해주세요</span>
      </S.Paragraph>
      <S.Form {...onSubmit(handleSubmit)}>
        <S.InputBox>
          <S.NumberInput
            type="number"
            placeholder={isTimeOver ? '인증시간 만료' : '123456'}
            {...register('code', {
              required: true,
              watch: true,
              customValidations: [
                {
                  validate: (value) => /^[0-9]{6}$/.test(value),
                  validationMessage: '입력값이 유효하지 않습니다.',
                },
              ],
            })}
            disabled={isTimeOver}
          />
          <S.ExpiredTimeParagraph>{remainTime}</S.ExpiredTimeParagraph>
        </S.InputBox>
        {errors['code'] || !values['code'] || isTimeOver ? (
          <S.ResendButton onClick={handleReSendClick}>
            인증번호 재요청
          </S.ResendButton>
        ) : (
          <S.ConfirmButton disabled={isSubmitting}>확인</S.ConfirmButton>
        )}
      </S.Form>
      <S.CloseButton onClick={onDismiss}>Ⅹ</S.CloseButton>
    </S.Layout>
  );
};

export default EmailConfirmModal;
