import React, { useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import * as S from './RegisterPage.styled';
import Input from 'components/@shared/Input';
import InputHint from 'components/@shared/InputHint';
import { userContext, UserContextValues } from 'contexts/userContext';
import useForm from 'hooks/useForm';
import useQuery from 'hooks/useQuery';
import useMutation from 'hooks/useMutation';
import { UserRegisterRequestBody } from 'types/userType';
import { checkEmailApi, submitRegisterApi } from 'apis/userApis';

const RegisterPage = () => {
  const navigate = useNavigate();
  const { login } = useContext(userContext) as UserContextValues;
  const { values, errors, isSubmitting, onSubmit, register } = useForm();
  const [isEmailExist, setIsEmailExist] = useState(true);

  const { refetch: checkEmailRefetch } = useQuery(
    ['checkEmail'],
    checkEmailApi(values['email'] as string),
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
        alert('이메일 중복확인을 실패했습니다.');
      },
    }
  );

  const { mutate: registerMutate } = useMutation(submitRegisterApi, {
    onSuccess: ({ body: userData, accessToken }) => {
      login(userData, accessToken);
      navigate('/');
    },
    onError: () => {
      alert('회원가입을 실패했습니다.');
    },
  });

  const handleCheckEmailButtonClick: React.MouseEventHandler<
    HTMLButtonElement
  > = () => {
    checkEmailRefetch();
  };

  const handleSubmit: React.FormEventHandler<HTMLFormElement> = async (e) => {
    if (isEmailExist) {
      throw e;
    }

    const target = e.target as HTMLFormElement;
    const formData = new FormData(target);
    formData.delete('passwordConfirm');
    const formDataObject = Object.fromEntries(
      formData.entries()
    ) as UserRegisterRequestBody;

    await registerMutate(formDataObject);
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
                  watch: true,
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
                patternValidationMessage:
                  '8에서 30자리 이하의 영어, 숫자, 특수문자로 입력해주세요.',
                onChange: (e, inputContoller) => {
                  inputContoller['passwordConfirm'].checkValidity();
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
                customValidations: [
                  {
                    validate: (value, inputController) =>
                      inputController['password'] &&
                      inputController['passwordConfirm'] &&
                      inputController['password'].element.value ===
                        inputController['passwordConfirm'].element.value,
                    validationMessage: '비밀번호가 다릅니다.',
                  },
                ],
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
        </S.FieldBox>
        <S.FieldBox>
          <S.Label>
            닉네임
            <Input
              type="text"
              {...register('nickname', {
                maxLength: 15,
                pattern: '([a-zA-Z0-9가-힣]){1,15}',
                patternValidationMessage:
                  '15자 이하의 영어, 한글, 숫자 조합으로 입력해주세요.',
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
