import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import * as S from './RegisterPage.styled';
import Input from 'components/@shared/Input';
import InputHint from 'components/@shared/InputHint';
import useForm from 'hooks/useForm';
import useContextValues from 'hooks/useContextValues';
import { userContext, UserContextValues } from 'contexts/userContext';
import { GetMeDataResponseBody } from 'types/userType';

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

const getMeData = (url: string, accessToken: string) => {
  return fetch(url, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
    },
  });
};

const RegisterPage = () => {
  const navigate = useNavigate();
  const { setUser } = useContextValues<UserContextValues>(
    userContext
  ) as UserContextValues;
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
    formData.delete('passwordConfirm');
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

    localStorage.setItem('accessToken', accessToken);

    const getMeResponse = await getMeData('/users/me', accessToken);
    if (!getMeResponse.ok) {
      alert('내 정보 가져오기 실패');
      return;
    }

    const MeData = (await getMeResponse.json()) as GetMeDataResponseBody;

    setUser(MeData);
    navigate('/');
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
                })}
              />
              <S.EmailCheckButton
                type="button"
                variant="confirm"
                onClick={handleCheckEmailButtonClick}
                disabled={!isEmailExist}
              >
                중복확인
              </S.EmailCheckButton>
            </S.EmailBox>
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
          <InputHint
            isShow={errors['password'] !== ''}
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
                required: true,
              })}
            />
          </S.Label>
          <InputHint
            isShow={errors['passwordConfirm'] !== ''}
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
                required: true,
              })}
            />
          </S.Label>
          <InputHint
            isShow={errors['nickname'] !== ''}
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
          <S.LoginLink href="">로그인</S.LoginLink>
        </S.LoginHintParagraph>
      </S.ButtonBox>
    </S.Layout>
  );
};

export default RegisterPage;
