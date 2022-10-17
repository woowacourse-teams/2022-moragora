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
  const [timeOver, setTimeOver] = useState(false);
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
    refetch({ email });
  };

  const handleSubmit = () => {
    if (!errors['code'] && !timeOver) {
      codeVerifyMutation.mutate({
        email,
        verifyCode: values['code'] as string,
      });
    }
  };

  useEffect(() => {
    if (expiredTimestamp - currentTimestamp <= 0) {
      setTimeOver(true);
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
            {...register('code', {
              type: 'number',
              placeholder: '123456',
              required: true,
              watch: true,
              customValidations: [
                {
                  validate: (value) => /^[0-9]{6}$/.test(value),
                  validationMessage: '입력값이 유효하지 않습니다.',
                },
              ],
            })}
          />
          <S.ExpiredTimeParagraph>{remainTime}</S.ExpiredTimeParagraph>
        </S.InputBox>
        {errors['code'] || !values['code'] ? (
          <S.Button onClick={handleReSendClick}>인증번호 재요청</S.Button>
        ) : (
          <S.Button disabled={isSubmitting || timeOver}>확인</S.Button>
        )}
      </S.Form>
      <S.CloseButton onClick={onDismiss}>Ⅹ</S.CloseButton>
    </S.Layout>
  );
};

export default EmailConfirmModal;
