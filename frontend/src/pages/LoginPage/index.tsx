import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import * as S from './LoginPage.styled';
import useForm from 'hooks/useForm';
import Input from 'components/@shared/Input';
import InputHint from 'components/@shared/InputHint';
import {
  GetLoginUserDataResponseBody,
  UserLoginRequestBody,
  UserLoginResponseBody,
} from 'types/userType';
import { userContext, UserContextValues } from 'contexts/userContext';
import useContextValues from 'hooks/useContextValues';
import request from 'utils/request';
import useMutation from 'hooks/useMutation';
import useQuery from 'hooks/useQuery';

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

const LoginPage = () => {
  const navigate = useNavigate();
  const { errors, isSubmitting, onSubmit, register } = useForm();
  const [accessToken, setAccessToken] = useState('');
  const { login, logout } = useContextValues<UserContextValues>(
    userContext
  ) as UserContextValues;
  const { isSuccess: isLoginSuccess, mutate } = useMutation<
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

  const handleSubmit: React.FormEventHandler<HTMLFormElement> = async (e) => {
    const target = e.target as HTMLFormElement;
    const formData = new FormData(target);
    const formDataObject = Object.fromEntries(
      formData.entries()
    ) as UserLoginRequestBody;

    mutate(formDataObject);
  };

  return (
    <S.Layout>
      <S.Form id="login-form" {...onSubmit(handleSubmit)}>
        <S.FieldBox>
          <S.Label>
            이메일
            <Input type="email" {...register('email', { required: true })} />
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
                required: true,
              })}
            />
          </S.Label>
          <InputHint
            isShow={Boolean(errors['password']) && errors['password'] !== ''}
            message={errors['password']}
          />
        </S.FieldBox>
      </S.Form>
      <S.ButtonBox>
        <S.LoginButton type="submit" form="login-form" disabled={isSubmitting}>
          로그인
        </S.LoginButton>
        <S.RegisterHintParagraph>
          모라고라가 처음이신가요?
          <S.RegisterLink to="/register">회원가입</S.RegisterLink>
        </S.RegisterHintParagraph>
      </S.ButtonBox>
    </S.Layout>
  );
};

export default LoginPage;
