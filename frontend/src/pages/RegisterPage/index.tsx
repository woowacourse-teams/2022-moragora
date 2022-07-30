import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import * as S from './RegisterPage.styled';
import Input from 'components/@shared/Input';
import InputHint from 'components/@shared/InputHint';
import useForm from 'hooks/useForm';
import useContextValues from 'hooks/useContextValues';
import { userContext, UserContextValues } from 'contexts/userContext';
import { UserRegisterRequestBody } from 'types/userType';
import useQuery from 'hooks/useQuery';
import useMutation from 'hooks/useMutation';
import { checkEmailApi, submitRegisterApi } from 'utils/Apis/userApis';

const RegisterPage = () => {
  const navigate = useNavigate();
  const { login } = useContextValues<UserContextValues>(
    userContext
  ) as UserContextValues;
  const { values, errors, isSubmitting, onSubmit, register } = useForm();
  const [isEmailExist, setIsEmailExist] = useState(true);
  const [isValidPasswordConfirm, setIsValidPasswordConfirm] = useState(true);

  const { refetch: checkEmailRefetch } = useQuery(
    ['checkEmail'],
    checkEmailApi(values['email']),
    {
      enabled: false,
      onSuccess: ({ body: { isExist } }) => {
        setIsEmailExist(isExist);
        const message = isExist
          ? '중복된 이메일입니다.'
          : '사용 가능한 이메일입니다.';

        alert(message);
      },
      onError: () => {
        alert('이메일 중복확인 실패');
      },
    }
  );

  const { mutate: registerMutate } = useMutation(submitRegisterApi, {
    onSuccess: ({ body: userData, accessToken }) => {
      login(userData, accessToken);
      navigate('/');
    },
    onError: () => {
      alert('회원가입 실패');
    },
  });

  const handleCheckEmailButtonClick: React.MouseEventHandler<
    HTMLButtonElement
  > = () => {
    checkEmailRefetch();
  };

  const handleSubmit: React.FormEventHandler<HTMLFormElement> = async (e) => {
    if (!isValidPasswordConfirm || isEmailExist) {
      return;
    }

    const target = e.target as HTMLFormElement;
    const formData = new FormData(target);
    formData.delete('passwordConfirm');
    const formDataObject = Object.fromEntries(
      formData.entries()
    ) as UserRegisterRequestBody;

    registerMutate(formDataObject);
  };

  return (
    <S.Layout>
      <S.Form id="register-form" {...onSubmit(handleSubmit)}>
        <S.FieldBox>
          <S.Label>
            이메일
            <S.EmailBox>
              <S.EmailInput
                type="email"
                {...register('email', {
                  required: true,
                  onChange: () => {
                    setIsEmailExist(true);
                  },
                  maxLength: 50,
                })}
                placeholder="이메일을 입력해주세요."
              />
              <S.EmailCheckButton
                type="button"
                variant="confirm"
                onClick={handleCheckEmailButtonClick}
                disabled={
                  !values['email'] || errors['email'] !== '' || !isEmailExist
                }
              >
                중복확인
              </S.EmailCheckButton>
            </S.EmailBox>
          </S.Label>
          <InputHint
            isShow={Boolean(errors['email']) && errors['email'] !== ''}
            message={errors['email']}
          />
        </S.FieldBox>
        <S.FieldBox>
          <S.Label>
            비밀번호
            <Input
              type="password"
              {...register('password', {
                pattern:
                  '(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,30}',
                onChange: (e) => {
                  setIsValidPasswordConfirm(
                    values['passwordConfirm'] === e.target.value
                  );
                },
                minLength: 8,
                maxLength: 30,
                required: true,
              })}
              placeholder="8에서 30자리 이하의 영어, 숫자, 특수문자로 입력해주세요."
            />
          </S.Label>
          <InputHint
            isShow={Boolean(errors['password']) && errors['password'] !== ''}
            message={errors['password']}
          />
        </S.FieldBox>
        <S.FieldBox>
          <S.Label>
            비밀번호 확인
            <Input
              type="password"
              {...register('passwordConfirm', {
                onChange: (e) => {
                  setIsValidPasswordConfirm(
                    values['password'] === e.target.value
                  );
                },
                minLength: 8,
                maxLength: 30,
                required: true,
              })}
            />
          </S.Label>
          <InputHint
            isShow={
              Boolean(errors['passwordConfirm']) &&
              errors['passwordConfirm'] !== ''
            }
            message={errors['passwordConfirm']}
          />
          <InputHint
            isShow={!isValidPasswordConfirm}
            message="비밀번호가 다릅니다."
          />
        </S.FieldBox>
        <S.FieldBox>
          <S.Label>
            닉네임
            <Input
              type="text"
              {...register('nickname', {
                maxLength: 15,
                pattern: '([a-zA-Z0-9가-힣]){1,15}',
                required: true,
              })}
              placeholder="15자 이하의 영어, 한글, 숫자 조합으로 입력해주세요."
            />
          </S.Label>
          <InputHint
            isShow={Boolean(errors['nickname']) && errors['nickname'] !== ''}
            message={errors['nickname']}
          />
        </S.FieldBox>
      </S.Form>
      <S.ButtonBox>
        <S.RegisterButton
          type="submit"
          form="register-form"
          disabled={isSubmitting}
        >
          회원가입
        </S.RegisterButton>
        <S.LoginHintParagraph>
          이미 가입된 계정이 있으신가요?
          <S.LoginLink to="/login">로그인</S.LoginLink>
        </S.LoginHintParagraph>
      </S.ButtonBox>
    </S.Layout>
  );
};

export default RegisterPage;
