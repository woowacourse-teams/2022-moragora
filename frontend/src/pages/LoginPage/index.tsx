import React from 'react';
import { useNavigate } from 'react-router-dom';
import * as S from './LoginPage.styled';
import useForm from 'hooks/useForm';
import Input from 'components/@shared/Input';
import InputHint from 'components/@shared/InputHint';
import { GetMeDataResponseBody, User } from 'types/userType';
import { userContext, UserContextValues } from 'contexts/userContext';
import useContextValues from 'hooks/useContextValues';
import { TOKEN_ERROR_STATUS_CODES } from 'consts';

const submitLogin = (
  url: string,
  payload: Pick<User, 'email' | 'password'>
) => {
  return fetch(`${process.env.API_SERVER_HOST}${url}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  });
};

const getMeData = (url: string, accessToken: string) => {
  return fetch(`${process.env.API_SERVER_HOST}${url}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${accessToken}`,
    },
  });
};

const LoginPage = () => {
  const navigate = useNavigate();
  const { login, logout } = useContextValues<UserContextValues>(
    userContext
  ) as UserContextValues;
  const { errors, isSubmitting, onSubmit, register } = useForm();

  const handleSubmit: React.FormEventHandler<HTMLFormElement> = async (e) => {
    const target = e.target as HTMLFormElement;
    const formData = new FormData(target);
    const formDataObject = Object.fromEntries(formData.entries()) as Pick<
      User,
      'email' | 'password'
    >;

    const loginResponse = await submitLogin('/login', formDataObject);
    if (!loginResponse.ok) {
      alert('로그인 실패');
      return;
    }

    const accessToken = await loginResponse
      .json()
      .then((data) => data.accessToken);

    const getMeResponse = await getMeData('/users/me', accessToken);

    if (!getMeResponse.ok) {
      if (TOKEN_ERROR_STATUS_CODES.includes(getMeResponse.status)) {
        logout();
      }

      alert('내 정보 가져오기 실패');
      return;
    }

    const MeData = (await getMeResponse.json()) as GetMeDataResponseBody;

    login(MeData, accessToken);
    navigate('/');
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
