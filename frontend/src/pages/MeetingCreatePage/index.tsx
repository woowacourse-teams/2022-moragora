import React from 'react';
import Button from '../../components/@shared/Button';
import Footer from '../../components/layouts/Footer';
import Input from '../../components/@shared/Input';
import * as S from './MeetingCreatePage.styled';

const MeetingCreatePage = () => {
  return (
    <>
      <S.Layout>
        <S.TitleSection>
          <h2>새로운 모임 만들기</h2>
        </S.TitleSection>
        <S.Form
          id="meeting-create-form"
          onSubmit={(e) => {
            e.preventDefault();

            const target = e.target as HTMLFormElement;
            const formData = new FormData(target);
            const formDataObject = Object.fromEntries(formData.entries());

            console.log(formDataObject);
          }}
        >
          <S.FieldBox>
            <S.Label>
              모임명
              <Input type="text" name="title" required />
            </S.Label>
          </S.FieldBox>
          <S.FieldBox>
            <S.Label>
              팀원
              <Input type="text" name="member" />
            </S.Label>
          </S.FieldBox>
          <S.FieldBox>
            <S.Label>
              시작 날짜
              <Input
                type="date"
                name="start-date"
                onClick={(e: React.MouseEvent<HTMLInputElement>) => {
                  const target = e.target as HTMLInputElement & {
                    showPicker: () => void;
                  };

                  target.showPicker();
                }}
                required
              />
            </S.Label>
            <S.Label>
              마감 날짜
              <Input
                type="date"
                name="end-date"
                onClick={(e: React.MouseEvent<HTMLInputElement>) => {
                  const target = e.target as HTMLInputElement & {
                    showPicker: () => void;
                  };

                  target.showPicker();
                }}
                required
              />
            </S.Label>
          </S.FieldBox>
          <S.FieldBox>
            <S.Label>
              시작 시간
              <Input
                type="time"
                name="start-time"
                onClick={(e: React.MouseEvent<HTMLInputElement>) => {
                  const target = e.target as HTMLInputElement & {
                    showPicker: () => void;
                  };

                  target.showPicker();
                }}
                required
              />
            </S.Label>
            <S.Label>
              마감 시간
              <Input
                type="time"
                name="end-time"
                onClick={(e: React.MouseEvent<HTMLInputElement>) => {
                  const target = e.target as HTMLInputElement & {
                    showPicker: () => void;
                  };

                  target.showPicker();
                }}
                required
              />
            </S.Label>
          </S.FieldBox>
        </S.Form>
      </S.Layout>
      <Footer>
        <Button form="meeting-create-form" type="submit">
          추가하기
        </Button>
      </Footer>
    </>
  );
};

export default MeetingCreatePage;
