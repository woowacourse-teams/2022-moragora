import React from 'react';
import * as S from './MeetingCreatePage.styled';
import Footer from 'components/layouts/Footer';
import Input from 'components/@shared/Input';
import MemberAddInput from 'components/MemberAddInput';
import InputHint from 'components/@shared/InputHint';
import useForm from 'hooks/useForm';
import useQuerySelectItems from 'hooks/useQuerySelectItems';
import { UserQueryWithKeywordResponse } from 'types/userType';
import { dateToFormattedString } from 'utils/timeUtil';

const MAX_SELECTED_USER_COUNT = 50;

const MeetingCreatePage = () => {
  const { values, errors, isSubmitting, onSubmit, register } = useForm();
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
  const currentDate = new Date();
  const isParticipantSelected = selectedItems.length > 0;

  const handleCreateMeetingSubmit: React.FormEventHandler<HTMLFormElement> = (
    e
  ) => {
    if (!isParticipantSelected) {
      return;
    }

    const userIds = selectedItems.map(({ id }) => id);

    const target = e.target as HTMLFormElement;
    const formData = new FormData(target);
    const formDataObject = {
      ...Object.fromEntries(formData.entries()),
      userIds,
    };

    console.log(formDataObject);
  };

  return (
    <>
      <S.Layout>
        <S.Form
          id="meeting-create-form"
          {...onSubmit(handleCreateMeetingSubmit)}
        >
          <S.FieldBox>
            <S.Label>
              모임명
              <Input
                type="text"
                {...register('title', { maxLength: 50, required: true })}
              />
            </S.Label>
            <InputHint
              isShow={errors['title'] !== ''}
              message={errors['title']}
            />
          </S.FieldBox>
          <S.FieldGroupBox>
            <S.FieldBox>
              <S.Label>
                시작 날짜
                <Input
                  type="date"
                  {...register('start-date', {
                    onClick: (e) => {
                      const target = e.target as HTMLInputElement & {
                        showPicker: () => void;
                      };

                      target.showPicker();
                    },
                    min: dateToFormattedString(currentDate),
                    required: true,
                  })}
                />
              </S.Label>
              <InputHint
                isShow={errors['start-date'] !== ''}
                message={errors['start-date']}
              />
            </S.FieldBox>
            <S.FieldBox>
              <S.Label>
                마감 날짜
                <Input
                  type="date"
                  {...register('end-date', {
                    onClick: (e) => {
                      const target = e.target as HTMLInputElement & {
                        showPicker: () => void;
                      };

                      target.showPicker();
                    },
                    min: values['start-date']
                      ? values['start-date']
                      : dateToFormattedString(currentDate),
                    required: true,
                  })}
                  disabled={errors['start-date'] !== ''}
                />
              </S.Label>
              <InputHint
                isShow={errors['end-date'] !== ''}
                message={errors['end-date']}
              />
            </S.FieldBox>
          </S.FieldGroupBox>
          <S.FieldGroupBox>
            <S.FieldBox>
              <S.Label>
                시작 시간
                <Input
                  type="time"
                  {...register('start-time', {
                    onClick: (e) => {
                      const target = e.target as HTMLInputElement & {
                        showPicker: () => void;
                      };

                      target.showPicker();
                    },
                    required: true,
                  })}
                />
              </S.Label>
              <InputHint
                isShow={errors['start-time'] !== ''}
                message={errors['start-time']}
              />
            </S.FieldBox>
            <S.FieldBox>
              <S.Label>
                마감 시간
                <Input
                  type="time"
                  {...register('end-time', {
                    onClick: (e) => {
                      const target = e.target as HTMLInputElement & {
                        showPicker: () => void;
                      };

                      target.showPicker();
                    },
                    required: true,
                  })}
                  disabled={errors['start-time'] !== ''}
                />
              </S.Label>
              <InputHint
                isShow={errors['end-time'] !== ''}
                message={errors['end-time']}
              />
            </S.FieldBox>
          </S.FieldGroupBox>
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
          <InputHint
            isShow={!isParticipantSelected}
            message="참여자가 선택되지 않았습니다."
          />
        </S.Form>
        <S.MeetingCreateButton
          form="meeting-create-form"
          type="submit"
          disabled={isSubmitting}
        >
          모임 생성하기
        </S.MeetingCreateButton>
      </S.Layout>
      <Footer></Footer>
    </>
  );
};

export default MeetingCreatePage;
