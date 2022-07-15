import React, { useState } from 'react';
import Input from '../../components/@shared/Input';
import Button from '../../components/@shared/Button';
import * as S from './RegisterPage.styled';
import { css } from '@emotion/react';
import useForm from '../../hooks/useForm';
import DialogButton from '../../components/@shared/DialogButton';
import { useNavigate } from 'react-router-dom';

const checkEmail = async (url: string) => {
  return fetch(url, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  });
};
const submitData = async (url: string, payload: any) => {
  return fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  });
};

const RegisterPage = () => {
  const navigate = useNavigate();
  const { values, errors, isSubmitting, onSubmit, register } = useForm();
  const [isEmailExist, setIsEmailExist] = useState(true);
  const [isValidPasswordConfirm, setIsValidPasswordConfirm] = useState(true);

  const handleCheckEmailButtonClick: React.MouseEventHandler<
    HTMLButtonElement
  > = async () => {
    const res = await checkEmail(`/users/check-email?email=${values['email']}`);
    const body = await res.json().then((res) => res as { isExist: boolean });

    if (!res.ok) {
      throw Error('이메일 중복 확인 실패');
    }

    setIsEmailExist(body.isExist);

    const message = body.isExist
      ? '중복된 이메일입니다.'
      : '사용 가능한 이메일입니다.';

    alert(message);
  };

  const handleSubmit: React.FormEventHandler<HTMLFormElement> = async (e) => {
    if (!isValidPasswordConfirm && isEmailExist) {
      return;
    }

    const target = e.target as HTMLFormElement;
    const formData = new FormData(target);
    const formDataObject = Object.fromEntries(formData.entries());

    const registerRes = await submitData('/users', formDataObject);

    if (!registerRes.ok) {
      throw Error('이메일 중복 확인 실패');
    }

    alert('회원가입 완료');

    const loginRes = await submitData('/login', {
      email: formDataObject.email,
      password: formDataObject.password,
    });
    if (!loginRes.ok) {
      alert('로그인 실패');
      return;
    }

    const accessToken = await loginRes.json().then((data) => data.accessToken);

    navigate('/meeting');
  };

  return (
    <S.Layout>
      <S.Form id="register-form" {...onSubmit(handleSubmit)}>
        <S.FieldBox>
          <S.Label>
            이메일
            <div
              css={css`
                display: flex;
                gap: 0.75rem;
              `}
            >
              <Input
                type="email"
                {...register('email', {
                  required: true,
                  onChange: () => {
                    setIsEmailExist(true);
                  },
                })}
                css={css`
                  flex: 1;
                `}
              />
              <DialogButton
                css={css`
                  border-radius: 0.5rem;
                `}
                variant="confirm"
                onClick={handleCheckEmailButtonClick}
                disabled={!isEmailExist}
              >
                중복확인
              </DialogButton>
            </div>
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
                  '(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,20}',
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
            <S.InputHint>비밀번호가 다릅니다.</S.InputHint>
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
