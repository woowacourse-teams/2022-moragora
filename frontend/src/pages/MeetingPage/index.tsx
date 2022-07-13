import React, { useState } from 'react';
import * as S from './MeetingPage.styled';
import Footer from '../../components/layouts/Footer';
import Button from '../../components/@shared/Button';
import { css } from '@emotion/react';
import useFetch from '../../hooks/useFetch';
import Spinner from '../../components/@shared/Spinner';
import ErrorIcon from '../../components/@shared/ErrorIcon';
import ModalPortal from '../../components/ModalPortal';
import ModalWindow from '../../components/@shared/ModalWindow';
import DivideLine from '../../components/@shared/DivideLine';
import ReloadButton from '../../components/@shared/ReloadButton';
import useForm from '../../hooks/useForm';
import CoffeeIconSVG from '../../assets/coffee.svg';
import Checkbox from '../../components/@shared/Checkbox';

type MeetingResponseBody = {
  meetingCount: number;
};

type UsersResponseBody = {
  id: number;
  name: string;
  absentCount: number;
}[];

type FormDataObject = { [k: string]: FormDataEntryValue };

const submitAttendenceData = async (url: string, payload: any) => {
  return fetch(url, {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  });
};

const MeetingPage = () => {
  const meetingState = useFetch<MeetingResponseBody>('/meetings/1');
  const usersState = useFetch<UsersResponseBody>('/meetings/1/users');
  const [modalOpened, setModalOpened] = useState(false);
  const [formData, setFormData] = useState<FormDataObject>();
  const { isSubmitting, onSubmit, register } = useForm();

  const handleOpen = () => {
    setModalOpened(true);
  };

  const handleClose = () => {
    setModalOpened(false);
  };

  const handleSubmit: React.FormEventHandler<HTMLFormElement> = (e) => {
    const target = e.target as HTMLFormElement;
    const formData = new FormData(target);
    const formDataObject = Object.fromEntries(formData.entries());

    setFormData(formDataObject);

    handleOpen();
  };

  const handleConfirm = async () => {
    const payload = Object.entries(formData).map(([id, value]) => ({
      id,
      isAbsent: value === 'absent',
    }));

    const response = await submitAttendenceData('/meetings/1', payload);

    if (response.ok) {
      alert('출결 마감했습니다.');
      handleClose();

      meetingState.refetch();
      usersState.refetch();
    }
  };

  if (meetingState.loading || usersState.loading) {
    return (
      <>
        <S.Layout>
          <div
            css={css`
              flex: 1;
              display: flex;
              justify-content: center;
              align-items: center;
            `}
          >
            <Spinner />
          </div>
        </S.Layout>
        <Footer>
          <Button form="attendance-form" type="submit" disabled>
            출결 마감
          </Button>
        </Footer>
      </>
    );
  }

  if (meetingState.error || usersState.error) {
    return (
      <>
        <S.Layout>
          <div
            css={css`
              flex: 1;
              display: flex;
              flex-direction: column;
              justify-content: center;
              align-items: center;
              gap: 2rem;
            `}
          >
            <ErrorIcon />
            <ReloadButton
              onClick={() => {
                meetingState.refetch();
                usersState.refetch();
              }}
            />
          </div>
        </S.Layout>
        <Footer>
          <Button form="attendance-form" type="submit" disabled>
            출결 마감
          </Button>
        </Footer>
      </>
    );
  }

  return (
    <>
      {modalOpened && (
        <ModalPortal closePortal={handleClose}>
          <ModalWindow
            message="마감하시겠습니까?"
            onConfirm={handleConfirm}
            onDismiss={handleClose}
          />
        </ModalPortal>
      )}
      <S.Layout>
        <S.MeetingDetailSection>
          <h2>모라고라팀</h2>
          <p>
            총 출석일: <span>{meetingState.data.meetingCount}</span>
          </p>
        </S.MeetingDetailSection>
        <DivideLine />
        <S.UserListSection>
          <S.Form id="attendance-form" {...onSubmit(handleSubmit)}>
            <S.UserList>
              {usersState.data.map((user) => (
                <S.UserItem key={user.id}>
                  <div
                    css={css`
                      display: flex;
                      flex-direction: column;
                      gap: 0.5rem;
                    `}
                  >
                    <span>{user.name}</span>
                    <S.CoffeeIconImageBox>
                      <S.CoffeeIconImage src={CoffeeIconSVG} />
                      <S.CoffeeIconImage src={CoffeeIconSVG} />
                      <S.CoffeeIconImage src={CoffeeIconSVG} />
                      <S.CoffeeIconImage src={CoffeeIconSVG} />
                      <S.CoffeeIconImage src={CoffeeIconSVG} />
                      <S.CoffeeIconImage src={CoffeeIconSVG} />
                    </S.CoffeeIconImageBox>
                  </div>
                  <Checkbox />
                </S.UserItem>
              ))}
            </S.UserList>
          </S.Form>
        </S.UserListSection>
      </S.Layout>
      <Footer>
        <Button form="attendance-form" type="submit" disabled={isSubmitting}>
          출결 마감
        </Button>
      </Footer>
    </>
  );
};

export default MeetingPage;
