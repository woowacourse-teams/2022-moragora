import React, { useState } from 'react';
import Input from '../../components/@shared/Input';
import Button from '../../components/@shared/Button';
import * as S from './RegisterPage.styled';
import { css } from '@emotion/react';
import useForm from '../../hooks/useForm';

const RegisterPage = () => {
  const { values, errors, isSubmitting, onSubmit, register } = useForm();
  const [isValidPasswordConfirm, setIsValidPasswordConfirm] = useState(true);

  return (
    <S.Layout>
      <S.Form
        id="register-form"
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
                onChange: (e) => {
                  setIsValidPasswordConfirm(
                    values['passwordConfirm'] === e.target.value
                  );
                },
                required: true,
              })}
            />
          </S.Label>
          {errors['password'] !== '' && (
            <S.InputHint>{errors['password']}</S.InputHint>
          )}
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
                required: true,
              })}
            />
          </S.Label>
          {errors['passwordConfirm'] !== '' && (
            <S.InputHint>{errors['passwordConfirm']}</S.InputHint>
          )}
          {!isValidPasswordConfirm && (
            <S.InputHint>아무고토 못하쥬?</S.InputHint>
          )}
        </S.FieldBox>
        <S.FieldBox>
          <S.Label>
            닉네임
            <Input
              type="text"
              {...register('nickname', {
                required: true,
              })}
            />
          </S.Label>
          {errors['nickname'] !== '' && (
            <S.InputHint>{errors['nickname']}</S.InputHint>
          )}
        </S.FieldBox>
      </S.Form>
      <S.ButtonBox>
        <Button
          type="submit"
          form="register-form"
          css={css`
            width: 382px;
          `}
          disabled={isSubmitting}
        >
          회원가입
        </Button>
        <S.LoginHintParagraph>
          이미 가입된 계정이 있으신가요?
          <S.LoginLink href="">로그인</S.LoginLink>
        </S.LoginHintParagraph>
      </S.ButtonBox>
    </S.Layout>
  );
};

export default RegisterPage;
