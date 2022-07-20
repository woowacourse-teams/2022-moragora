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
import { useNavigate } from 'react-router-dom';
import { MeetingResponseBody } from 'types/meetingType';

type DefaultResponseBody = {};

const MAX_SELECTED_USER_COUNT = 129;

const asyncFetch = async <
  ResponseBody extends DefaultResponseBody = DefaultResponseBody
>(
  url: string,
  option?: RequestInit
) => {
  const res = await fetch(url, option);

  if (!res.ok) {
    throw new Error('모임 생성에 실패했습니다.');
  }

  const body = (await res.json().catch((e) => ({}))) as ResponseBody;

  return {
    res,
    body,
  };
};

const MeetingCreatePage = () => {
  const navigate = useNavigate();
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

  const handleCreateMeetingSubmit: React.FormEventHandler<
    HTMLFormElement
  > = async (e) => {
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

    try {
      const meatingCreateResult = await asyncFetch('/meetings', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formDataObject),
      });

      alert('모임 생성을 완료했습니다.');

      const meetingGetResult = await asyncFetch<MeetingResponseBody>(
        meatingCreateResult.res.headers.get('location') as string
      );

      navigate(`/meeting/${meetingGetResult.body.id}`);
    } catch (e) {
      console.error(e);
    }
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
                {...register('name', { maxLength: 50, required: true })}
              />
            </S.Label>
            <InputHint
              isShow={errors['name'] !== ''}
              message={errors['name']}
            />
          </S.FieldBox>
          <S.FieldGroupBox>
            <S.FieldBox>
              <S.Label>
                시작 날짜
                <Input
                  type="date"
                  {...register('startDate', {
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
                isShow={errors['startDate'] !== ''}
                message={errors['startDate']}
              />
            </S.FieldBox>
            <S.FieldBox>
              <S.Label>
                마감 날짜
                <Input
                  type="date"
                  {...register('endDate', {
                    onClick: (e) => {
                      const target = e.target as HTMLInputElement & {
                        showPicker: () => void;
                      };

                      target.showPicker();
                    },
                    min: values['startDate']
                      ? values['startDate']
                      : dateToFormattedString(currentDate),
                    required: true,
                  })}
                  disabled={errors['startDate'] !== ''}
                />
              </S.Label>
              <InputHint
                isShow={errors['endDate'] !== ''}
                message={errors['endDate']}
              />
            </S.FieldBox>
          </S.FieldGroupBox>
          <S.FieldGroupBox>
            <S.FieldBox>
              <S.Label>
                시작 시간
                <Input
                  type="time"
                  {...register('entranceTime', {
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
                isShow={errors['entranceTime'] !== ''}
                message={errors['entranceTime']}
              />
            </S.FieldBox>
            <S.FieldBox>
              <S.Label>
                마감 시간
                <Input
                  type="time"
                  {...register('leaveTime', {
                    onClick: (e) => {
                      const target = e.target as HTMLInputElement & {
                        showPicker: () => void;
                      };

                      target.showPicker();
                    },
                    required: true,
                  })}
                  disabled={errors['entranceTime'] !== ''}
                />
              </S.Label>
              <InputHint
                isShow={errors['leaveTime'] !== ''}
                message={errors['leaveTime']}
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
