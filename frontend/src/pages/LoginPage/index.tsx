import React from 'react';
import Input from '../../components/@shared/Input';
import Button from '../../components/@shared/Button';
import * as S from './LoginPage.styled';
import { css } from '@emotion/react';
import useForm from '../../hooks/useForm';

const LoginPage = () => {
  const { errors, isSubmitting, onSubmit, register } = useForm();

  return (
    <S.Layout>
      <S.Form
        id="login-form"
        {...onSubmit(() => {
          alert('submitted');
        })}
      >
        <S.FieldBox>
          <S.Label>
            이메일
            <Input type="email" {...register('email', { required: true })} />
          </S.Label>
          {errors['email'] !== '' && (
            <S.InputHint>{errors['email']}</S.InputHint>
          )}
        </S.FieldBox>
        <S.FieldBox>
          <S.Label>
            비밀번호
            <Input
              type="password"
              {...register('password', {
                pattern:
                  "(?=.[0-9])(?=.[a-z])(?=.[!@#&()\\-\\[{}\\]:;',?/~$^+=<>]).{8,30}",
                required: true,
              })}
            />
          </S.Label>
          {errors['password'] !== '' && (
            <S.InputHint>{errors['password']}</S.InputHint>
          )}
        </S.FieldBox>
      </S.Form>
      <S.ButtonBox>
        <Button
          type="submit"
          form="login-form"
          css={css`
            width: 382px;
          `}
          disabled={isSubmitting}
        >
          로그인
        </Button>
        <S.RegisterHintParagraph>
          모라고라가 처음이신가요?
          <S.RegisterLink href="">회원가입</S.RegisterLink>
        </S.RegisterHintParagraph>
      </S.ButtonBox>
    </S.Layout>
  );
};

export default LoginPage;
