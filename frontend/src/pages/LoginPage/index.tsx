import React, { useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import * as S from './LoginPage.styled';
import useForm from 'hooks/useForm';
import useMutation from 'hooks/useMutation';
import { userContext, UserContextValues } from 'contexts/userContext';
import Input from 'components/@shared/Input';
import InputHint from 'components/@shared/InputHint';
import Button from 'components/@shared/Button';
import { UserLoginRequestBody } from 'types/userType';
import { googleLoginApi, submitLoginApi } from 'apis/userApis';
import GoogleLoginButton from 'components/GoogleLoginButton';

const LoginPage = () => {
  const navigate = useNavigate();
  const { login } = useContext(userContext) as UserContextValues;
  const code = new URLSearchParams(location.search).get('code');
  const { errors, isSubmitting, onSubmit, register } = useForm();

  const { mutate: loginMutate } = useMutation(submitLoginApi, {
    onSuccess: ({ body, accessToken }) => {
      login(body, accessToken);
      navigate('/');
    },
    onError: () => {
      alert('로그인을 실패했습니다.');
    },
  });

  const { mutate: googleLoginMutate } = useMutation(googleLoginApi, {
    onSuccess: ({ body, accessToken }) => {
      login(body, accessToken);
    },
    onError: () => {
      alert('구글 로그인을 실패했습니다.');
    },
    onSettled: () => {
      navigate('/');
    },
  });

  const handleSubmit: React.FormEventHandler<HTMLFormElement> = async (e) => {
    const target = e.target as HTMLFormElement;
    const formData = new FormData(target);
    const formDataObject = Object.fromEntries(
      formData.entries()
    ) as UserLoginRequestBody;

    await loginMutate(formDataObject);
  };

  useEffect(() => {
    if (code) {
      googleLoginMutate({ code });
    }
  }, [code]);

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
        <Button type="submit" form="login-form" disabled={isSubmitting}>
          로그인
        </Button>
        <GoogleLoginButton />
        <S.RegisterHintParagraph>
          모라고라가 처음이신가요?
          <S.RegisterLink to="/register">회원가입</S.RegisterLink>
        </S.RegisterHintParagraph>
      </S.ButtonBox>
    </S.Layout>
  );
};

export default LoginPage;
