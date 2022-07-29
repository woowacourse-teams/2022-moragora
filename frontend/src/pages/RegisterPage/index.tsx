import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import * as S from './RegisterPage.styled';
import Input from 'components/@shared/Input';
import InputHint from 'components/@shared/InputHint';
import useForm from 'hooks/useForm';
import useContextValues from 'hooks/useContextValues';
import { userContext, UserContextValues } from 'contexts/userContext';
import {
  GetLoginUserDataResponseBody,
  UserLoginRequestBody,
  UserLoginResponseBody,
  UserRegisterRequestBody,
} from 'types/userType';
import request from 'utils/request';
import useQuery from 'hooks/useQuery';
import useMutation from 'hooks/useMutation';

const checkEmail = (email: string) => () =>
  request<{ isExist: boolean }>(`/users/check-email?email=${email}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  });

const submitRegister = (payload: UserRegisterRequestBody) =>
  request('/users', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  });

const submitLogin = (payload: UserLoginRequestBody) =>
  request<UserLoginResponseBody>('/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  });

const getLoginUserData = (accessToken: string) => () =>
  request<GetLoginUserDataResponseBody>('/users/me', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${accessToken}`,
    },
  });

const RegisterPage = () => {
  const navigate = useNavigate();
  const { login, logout } = useContextValues<UserContextValues>(
    userContext
  ) as UserContextValues;
  const { values, errors, isSubmitting, onSubmit, register } = useForm();
  const [accessToken, setAccessToken] = useState('');
  const [isEmailExist, setIsEmailExist] = useState(true);
  const [isValidPasswordConfirm, setIsValidPasswordConfirm] = useState(true);

  const { refetch: checkEmailRefetch } = useQuery<{ isExist: boolean }>(
    ['checkEmail'],
    checkEmail(values['email']),
    {
      enabled: false,
      onSuccess: (data) => {
        setIsEmailExist(data.isExist);
        const message = data.isExist
          ? '중복된 이메일입니다.'
          : '사용 가능한 이메일입니다.';

        alert(message);
      },
      onError: () => {
        alert('이메일 중복확인 실패');
      },
    }
  );

  const { mutate: RegisterMutate } = useMutation<
    unknown,
    UserRegisterRequestBody
  >(submitRegister, {
    onSuccess: (data, variables) => {
      alert('회원가입 완료');
      LoginMutate(variables);
    },
    onError: () => {
      alert('회원가입 실패');
    },
  });

  const { isSuccess: isLoginSuccess, mutate: LoginMutate } = useMutation<
    UserLoginResponseBody,
    UserLoginRequestBody
  >(submitLogin, {
    onSuccess: (data) => {
      if (data.accessToken) {
        setAccessToken(data.accessToken);
      }
    },
    onError: () => {
      alert('로그인 실패');
    },
  });

  useQuery<GetLoginUserDataResponseBody>(
    ['getLoginUserData'],
    getLoginUserData(accessToken),
    {
      enabled: isLoginSuccess,
      onSuccess: (data) => {
        login(data, accessToken);
        navigate('/');
      },
      onError: () => {
        logout();
        alert('내 정보 가져오기 실패');
      },
    }
  );

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

    RegisterMutate(formDataObject);
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
