import React from 'react';
import { useNavigate } from 'react-router-dom';
import Input from '../../components/@shared/Input';
import * as S from './LoginPage.styled';
import useForm from '../../hooks/useForm';
import InputHint from '../../components/@shared/InputHint';

const submitLogin = async (url: string, payload: any) => {
  return fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  });
};

const LoginPage = () => {
  const navigate = useNavigate();
  const { errors, isSubmitting, onSubmit, register } = useForm();

  const handleSubmit: React.FormEventHandler<HTMLFormElement> = async (e) => {
    const target = e.target as HTMLFormElement;
    const formData = new FormData(target);
    const formDataObject = Object.fromEntries(formData.entries());

    const res = await submitLogin('/login', formDataObject);
    if (!res.ok) {
      alert('로그인 실패');
      return;
    }

    const accessToken = await res.json().then((data) => data.accessToken);
    navigate('/meeting');
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
            isShow={errors['email'] !== ''}
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
            isShow={errors['password'] !== ''}
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
