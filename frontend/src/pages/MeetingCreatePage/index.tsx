import React from 'react';
import Footer from '../../components/layouts/Footer';
import Input from '../../components/@shared/Input';
import * as S from './MeetingCreatePage.styled';
import useQuerySelectItems from '../../hooks/useQuerySelectItems';
import MemberAddInput from '../../components/MemberAddInput';
import { UserQueryWithKeywordResponse } from 'types/userType';

const MAX_SELECTED_USER_COUNT = 3;

const MeetingCreatePage = () => {
  const {
    queryResult,
    selectedItems,
    queryWithKeyword,
    selectItem,
    unselectItem,
    clearQueryResult,
  } = useQuerySelectItems<UserQueryWithKeywordResponse>('/users?keyword=', {
    wait: 150,
    maxSelectCount: MAX_SELECTED_USER_COUNT,
  });

  const handleCreateMeetingSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    const target = e.target as HTMLFormElement;
    const formData = new FormData(target);
    const formDataObject = Object.fromEntries(formData.entries());

    console.log(formDataObject);
  };

  return (
    <>
      <S.Layout>
        <S.Form id="meeting-create-form" onSubmit={handleCreateMeetingSubmit}>
          <S.FieldBox>
            <S.Label>
              모임명
              <Input type="text" name="title" required />
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
          <S.FieldBox>
            <S.Label>
              <S.AddMemberParagraph>
                멤버 추가하기
                <span>
                  {selectedItems.length}/{MAX_SELECTED_USER_COUNT}
                </span>
              </S.AddMemberParagraph>
              <MemberAddInput
                placeholder="닉네임 또는 이메일로 검색하세요."
                disabled={selectedItems.length >= MAX_SELECTED_USER_COUNT}
                queryResult={queryResult}
                selectedItems={selectedItems}
                queryWithKeyword={queryWithKeyword}
                selectItem={selectItem}
                unselectItem={unselectItem}
                clearQueryResult={clearQueryResult}
              />
            </S.Label>
          </S.FieldBox>
        </S.Form>
        <S.MeetingCreateButton form="meeting-create-form" type="submit">
          모임 생성하기
        </S.MeetingCreateButton>
      </S.Layout>
      <Footer></Footer>
    </>
  );
};

export default MeetingCreatePage;
