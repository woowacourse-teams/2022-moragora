import React from 'react';
import * as S from './UnregisterPage.styled';
import Footer from 'components/layouts/Footer';
import useForm from 'hooks/useForm';
import Input from 'components/@shared/Input';
import InputHint from 'components/@shared/InputHint';
import Button from 'components/@shared/Button';

const UnregisterPage = () => {
  const confirmMessage = 'email@email.com/delete';
  const { errors, onSubmit, register, isSubmitting } = useForm();

  const handleUnregisterSubmitValid: React.FormEventHandler<
    HTMLFormElement
  > = ({ currentTarget }) => {
    const formData = new FormData(currentTarget);
    const formDataObject = Object.fromEntries(formData.entries());

    console.log(formDataObject);
  };

  return (
    <>
      <S.Layout>
        <S.Form id="unregister-form" {...onSubmit(handleUnregisterSubmitValid)}>
          <S.FieldBox>
            <S.Label>
              <p>
                정말 탈퇴하시겠습니까?
                <br />
                탈퇴된 계정은 복구할 수 없습니다.
                <br />
                회원 탈퇴를 원하면{' '}
                <S.ConfirmMessageSpan>{confirmMessage}</S.ConfirmMessageSpan>를
                입력해주세요.
              </p>
              <Input
                type="text"
                {...register('confirmMessage', {
                  pattern: `^${confirmMessage}$`,
                  patternValidationMessage: '확인 메시지를 입력해주세요.',
                  required: true,
                })}
              />
            </S.Label>
            <InputHint
              isShow={!!errors['confirmMessage']}
              message={errors['confirmMessage']}
            />
          </S.FieldBox>
          <S.FieldBox>
            <S.Label>
              비밀번호
              <Input
                type="password"
                {...register('password', { required: true })}
              />
            </S.Label>
            <InputHint
              isShow={!!errors['password']}
              message={errors['password']}
            />
          </S.FieldBox>
        </S.Form>
        <S.ButtonBox>
          <Button type="submit" form="unregister-form" disabled={isSubmitting}>
            회원 탈퇴
          </Button>
        </S.ButtonBox>
      </S.Layout>
      <Footer />
    </>
  );
};

export default UnregisterPage;
