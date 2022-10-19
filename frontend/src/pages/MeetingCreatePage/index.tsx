import React, { useContext, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { css } from '@emotion/react';
import * as S from './MeetingCreatePage.styled';
import Input from 'components/@shared/Input';
import MemberAddInput from 'components/MemberAddInput';
import InputHint from 'components/@shared/InputHint';
import useForm from 'hooks/useForm';
import useMutation from 'hooks/useMutation';
import useQuerySelectItems from 'hooks/useQuerySelectItems';
import useGeolocation from 'hooks/useGeolocation';
import useKakaoMap from 'hooks/useKakaoMap';
import BeaconItem from 'components/BeaconItem';
import { UserQueryWithKeywordResponse } from 'types/userType';
import { userContext, UserContextValues } from 'contexts/userContext';
import { createMeetingApi } from 'apis/meetingApis';
import { createBeaconsApi } from 'apis/beaconApis';
import DialogButton from 'components/@shared/DialogButton';

const MAX_SELECTED_USER_COUNT = 30;

const MeetingCreatePage = () => {
  const navigate = useNavigate();
  const userState = useContext(userContext) as UserContextValues;
  const { errors, isSubmitting, onSubmit, register } = useForm();
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
  const meetingIdRef = useRef<number | null>(null);
  const isParticipantSelected = selectedItems.length > 0;
  const { currentPosition, isLoading } = useGeolocation();
  const {
    mapContainerRef,
    beacons,
    removeBeacon,
    removeBeacons,
    setControllable,
    panTo,
  } = useKakaoMap();
  const mapOverlayRef = useRef<HTMLDivElement>(null);

  const beaconCreateMutation = useMutation(createBeaconsApi({ accessToken }), {
    onSuccess: () => {
      alert('모임 생성을 완료했습니다.');
      navigate(`/meeting/${meetingIdRef.current}`);
    },
    onError: () => {
      alert('비콘 등록에 실패했습니다.');
    },
  });

  const meetingCreateMutation = useMutation(createMeetingApi({ accessToken }), {
    onSuccess: ({ body: { id } }) => {
      meetingIdRef.current = id;
      beaconCreateMutation.mutate({
        meetingId: id,
        beacons: beacons.map((beacon) => ({
          latitude: beacon.position.Ma,
          longitude: beacon.position.La,
          radius: beacon.radius,
          address: beacon.address.address_name,
        })),
      });
    },
    onError: () => {
      alert('모임 생성을 실패했습니다.');
    },
  });

  const handleCreateMeetingSubmit: React.FormEventHandler<
    HTMLFormElement
  > = async ({ currentTarget }) => {
    if (!isParticipantSelected) {
      return;
    }

    const userIds = selectedItems.map(({ id }) => id);

    const formData = new FormData(currentTarget);
    const formDataObject = {
      ...Object.fromEntries(formData.entries()),
      userIds,
    };

    meetingCreateMutation.mutate({
      formDataObject,
    });
  };

  useEffect(() => {
    if (isLoading && mapOverlayRef.current) {
      mapOverlayRef.current.classList.add('loading');
    } else {
      mapOverlayRef.current?.classList.remove('loading');
      if (currentPosition) {
        panTo(
          currentPosition.coords.latitude,
          currentPosition.coords.longitude
        );
      }
    }

    setControllable(!isLoading);
  }, [
    isLoading,
    mapOverlayRef.current,
    setControllable,
    panTo,
    currentPosition,
  ]);

  return (
    <S.Layout>
      <S.Form id="meeting-create-form" {...onSubmit(handleCreateMeetingSubmit)}>
        <S.FieldBox>
          <S.Label>
            모임명
            <Input
              type="text"
              {...register('name', { maxLength: 50, required: true })}
            />
          </S.Label>
          <InputHint
            isShow={Boolean(errors['name']) && errors['name'] !== ''}
            message={errors['name']}
          />
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
              css={css`
                width: 100%;
              `}
              name="searchMember"
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
        <S.Label>비콘 등록하기</S.Label>
        <S.MapSection>
          <S.Map ref={mapContainerRef}>
            <S.MapOverlay ref={mapOverlayRef} className="loading">
              Loading...
            </S.MapOverlay>
          </S.Map>
        </S.MapSection>
        <S.BeaconDescription>
          원하는 위치를 클릭하세요. 출석 가능한 반경을 한번 더 클릭하면 비콘이
          추가됩니다.
        </S.BeaconDescription>
        <S.BeaconListBox>
          <S.BeaconListLengthBox>
            <S.BeaconCountParagraph>
              비콘 개수: {beacons.length}{' '}
              <S.BeaconCountMaximumSpan>/ 3</S.BeaconCountMaximumSpan>
            </S.BeaconCountParagraph>
            <DialogButton
              type="button"
              onClick={removeBeacons}
              variant="confirm"
            >
              리셋
            </DialogButton>
          </S.BeaconListLengthBox>
          <S.BeaconList>
            {beacons.map(({ id, position, address, radius }) => (
              <li key={id}>
                <BeaconItem
                  id={id}
                  position={position}
                  address={address}
                  radius={Math.round(radius)}
                  panTo={panTo}
                  remove={removeBeacon}
                />
              </li>
            ))}
          </S.BeaconList>
        </S.BeaconListBox>
      </S.Form>
      <S.MeetingCreateButton
        form="meeting-create-form"
        type="submit"
        disabled={isSubmitting}
      >
        모임 생성하기
      </S.MeetingCreateButton>
    </S.Layout>
  );
};

export default MeetingCreatePage;
