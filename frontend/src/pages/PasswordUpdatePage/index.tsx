import React from 'react';
import * as S from './PasswordUpdatePage.styled';
import useForm from 'hooks/useForm';
import Input from 'components/@shared/Input';
import InputHint from 'components/@shared/InputHint';
import Button from 'components/@shared/Button';
import useMutation from 'hooks/useMutation';
import { updatePasswordApi } from 'apis/userApis';
import { UserUpdatePasswordRequestBody } from 'types/userType';

const PasswordUpdatePage = () => {
  const { errors, onSubmit, register, isSubmitting } = useForm();
  const passwordUpdateMutation = useMutation(updatePasswordApi(), {
    onSuccess: () => {
      alert('비밀번호가 변경되었습니다.');
    },
    onError: (e) => {
      alert(e);
    },
  });

  const handlePasswordSubmitValid: React.FormEventHandler<HTMLFormElement> = ({
    currentTarget,
  }) => {
    const formData = new FormData(currentTarget);

    formData.delete('newPasswordConfirm');

    const formDataObject = Object.fromEntries(
      formData.entries()
    ) as UserUpdatePasswordRequestBody;

    passwordUpdateMutation.mutate(formDataObject);
  };

  const handlePasswordSubmitError: React.FormEventHandler<
    HTMLFormElement
  > = () => {
    alert('error');
  };

  return (
    <S.Layout>
      <S.Form
        id="password-update-form"
        {...onSubmit(handlePasswordSubmitValid, handlePasswordSubmitError)}
      >
        <S.FieldBox>
          <S.Label>
            현재 비밀번호
            <Input
              type="password"
              {...register('oldPassword', { required: true })}
            />
          </S.Label>
          <InputHint
            isShow={!!errors['oldPassword']}
            message={errors['oldPassword']}
          />
        </S.FieldBox>
        <S.FieldBox>
          <S.Label>
            새로운 비밀번호
            <Input
              type="password"
              {...register('newPassword', {
                onChange: (e, inputController) => {
                  inputController['newPasswordConfirm'].checkValidity();
                },
                pattern:
                  '(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,30}',
                patternValidationMessage:
                  '8에서 30자리 이하의 영어, 숫자, 특수문자로 입력해주세요.',
                minLength: 8,
                maxLength: 30,
                required: true,
              })}
            />
          </S.Label>
          <InputHint
            isShow={!!errors['newPassword']}
            message={errors['newPassword']}
          />
        </S.FieldBox>
        <S.FieldBox>
          <S.Label>
            비밀번호 확인
            <Input
              type="password"
              {...register('newPasswordConfirm', {
                customValidations: [
                  {
                    validate: (value, inputController) =>
                      inputController['newPassword'] &&
                      inputController['newPasswordConfirm'] &&
                      inputController['newPassword'].element.value ===
                        inputController['newPasswordConfirm'].element.value,
                    validationMessage: '비밀번호가 다릅니다.',
                  },
                ],
                required: true,
              })}
            />
          </S.Label>
          <InputHint
            isShow={!!errors['newPasswordConfirm']}
            message={errors['newPasswordConfirm']}
          />
        </S.FieldBox>
      </S.Form>
      <S.ButtonBox>
        <Button
          type="submit"
          form="password-update-form"
          disabled={isSubmitting}
        >
          비밀번호 변경
        </Button>
      </S.ButtonBox>
    </S.Layout>
  );
};

export default PasswordUpdatePage;
