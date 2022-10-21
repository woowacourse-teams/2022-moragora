import React from 'react';
import { MeetingListResponseBody } from 'types/meetingType';
import crownIcon from 'assets/crown.svg';
import coffeeIcon from 'assets/simple-coffee.svg';
import { ArrayElement } from 'types/utilityType';
import { css } from '@emotion/react';
import * as S from './MeetingItem.styled';

export type MeetingItemProps = {
  meeting: ArrayElement<MeetingListResponseBody['meetings']>;
};

const MeetingItem: React.FC<MeetingItemProps> = ({ meeting }) => {
  let meetingStatusMessage = '';

  if (meeting.upcomingEvent) {
    meetingStatusMessage = meeting.isActive
      ? '체크인하세요!'
      : '출결 준비 중입니다.';
  } else {
    meetingStatusMessage = '다음 일정이 없습니다.';
  }

  return (
    <S.Layout isActive={meeting.isActive}>
      <S.MeetingItemLink to={`/meeting/${meeting.id}`}>
        <S.Box>
          <S.MeetingBox>
            <S.IconBox
              aria-label={`모임의 ${
                meeting.isLoginUserMaster ? '마스터' : '참여자'
              } ${meeting.isCoffeeTime && '커피스택 가득참'}`}
            >
              {meeting.isLoginUserMaster ? (
                <img
                  src={crownIcon}
                  alt=""
                  css={css`
                    width: 1rem;
                    height: 1rem;
                  `}
                />
              ) : (
                <S.IconSVG
                  width="1rem"
                  height="1rem"
                  viewBox="0 0 24 24"
                  fill="currentColor"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    fillRule="evenodd"
                    clipRule="evenodd"
                    d="M17.8868 12.1465C17.5188 12.1465 17.1978 11.8755 17.1448 11.5005C17.0878 11.0895 17.3728 10.7105 17.7838 10.6535C18.8238 10.5075 19.6088 9.60551 19.6108 8.55351C19.6108 7.51051 18.8628 6.62851 17.8318 6.45951C17.4238 6.39251 17.1468 6.00651 17.2138 5.59751C17.2818 5.18851 17.6648 4.91451 18.0758 4.97951C19.8338 5.26851 21.1108 6.77251 21.1108 8.55551C21.1068 10.3505 19.7658 11.8905 17.9908 12.1395C17.9558 12.1445 17.9208 12.1465 17.8868 12.1465Z"
                    fill="currentFill"
                  />
                  <mask
                    id="mask0_4_3750"
                    style={{ maskType: 'alpha' }}
                    maskUnits="userSpaceOnUse"
                    x="18"
                    y="14"
                    width="5"
                    height="5"
                  >
                    <path
                      fillRule="evenodd"
                      clipRule="evenodd"
                      d="M18.9781 14.0016H22.7721V18.5312H18.9781V14.0016Z"
                      fill="white"
                    />
                  </mask>
                  <g mask="url(#mask0_4_3750)">
                    <path
                      fillRule="evenodd"
                      clipRule="evenodd"
                      d="M20.8614 18.5312C20.5594 18.5312 20.2744 18.3472 20.1604 18.0482C20.0124 17.6612 20.2074 17.2272 20.5944 17.0802C21.2724 16.8222 21.2724 16.5372 21.2724 16.4002C21.2724 15.9622 20.7154 15.6562 19.6174 15.4922C19.2084 15.4302 18.9254 15.0492 18.9864 14.6382C19.0474 14.2282 19.4274 13.9552 19.8404 14.0082C22.2634 14.3712 22.7724 15.5092 22.7724 16.4002C22.7724 16.9442 22.5584 17.9372 21.1284 18.4822C21.0404 18.5152 20.9504 18.5312 20.8614 18.5312Z"
                      fill="currentFill"
                    />
                  </g>
                  <path
                    fillRule="evenodd"
                    clipRule="evenodd"
                    d="M11.8867 15.914C9.51073 15.914 6.67773 16.206 6.67773 17.596C6.67773 18.997 9.51073 19.291 11.8867 19.291C14.2627 19.291 17.0947 19 17.0947 17.613C17.0947 16.209 14.2627 15.914 11.8867 15.914ZM11.8867 20.791C10.2277 20.791 5.17773 20.791 5.17773 17.596C5.17773 14.414 10.2277 14.414 11.8867 14.414C13.5457 14.414 18.5947 14.414 18.5947 17.613C18.5947 20.791 13.7217 20.791 11.8867 20.791Z"
                    fill="currentFill"
                  />
                  <path
                    fillRule="evenodd"
                    clipRule="evenodd"
                    d="M11.8868 5.5C10.1948 5.5 8.81877 6.877 8.81877 8.569C8.81577 9.389 9.12977 10.156 9.70477 10.735C10.2798 11.314 11.0458 11.635 11.8618 11.638L11.8868 12.388V11.638C13.5788 11.638 14.9558 10.262 14.9558 8.569C14.9558 6.877 13.5788 5.5 11.8868 5.5ZM11.8868 13.138H11.8598C10.6398 13.134 9.49677 12.656 8.64077 11.792C7.78277 10.929 7.31377 9.783 7.31877 8.566C7.31877 6.05 9.36776 4 11.8868 4C14.4068 4 16.4558 6.05 16.4558 8.569C16.4558 11.088 14.4068 13.138 11.8868 13.138Z"
                    fill="currentFill"
                  />
                  <path
                    fillRule="evenodd"
                    clipRule="evenodd"
                    d="M5.88572 12.1465C5.85172 12.1465 5.81672 12.1445 5.78172 12.1395C4.00672 11.8905 2.66672 10.3505 2.66272 8.55751C2.66272 6.77251 3.93972 5.26851 5.69772 4.97951C6.11872 4.91351 6.49172 5.19051 6.55972 5.59751C6.62672 6.00651 6.34972 6.39251 5.94172 6.45951C4.91072 6.62851 4.16272 7.51051 4.16272 8.55551C4.16472 9.60551 4.94972 10.5085 5.98872 10.6535C6.39972 10.7105 6.68472 11.0895 6.62772 11.5005C6.57472 11.8755 6.25372 12.1465 5.88572 12.1465Z"
                    fill="currentFill"
                  />
                  <path
                    fillRule="evenodd"
                    clipRule="evenodd"
                    d="M2.91212 18.5312C2.82312 18.5312 2.73312 18.5152 2.64512 18.4822C1.21412 17.9362 1.00012 16.9432 1.00012 16.4002C1.00012 15.5102 1.50912 14.3712 3.93312 14.0082C4.34612 13.9562 4.72412 14.2282 4.78612 14.6382C4.84712 15.0492 4.56412 15.4302 4.15512 15.4922C3.05712 15.6562 2.50012 15.9622 2.50012 16.4002C2.50012 16.5372 2.50012 16.8212 3.17912 17.0802C3.56612 17.2272 3.76112 17.6612 3.61312 18.0482C3.49912 18.3472 3.21412 18.5312 2.91212 18.5312Z"
                    fill="currentFill"
                  />
                </S.IconSVG>
              )}
              {meeting.isCoffeeTime && (
                <img
                  src={coffeeIcon}
                  alt=""
                  css={css`
                    width: 1rem;
                    height: 1rem;
                  `}
                />
              )}
            </S.IconBox>
            <S.MeetingNameSpan>{meeting.name}</S.MeetingNameSpan>
            <S.MeetingTimeSpan
              aria-label={`${meeting.upcomingEvent?.meetingStartTime} 시작 ${meeting.upcomingEvent?.meetingEndTime} 마감`}
            >
              {meeting.upcomingEvent &&
                `${meeting.upcomingEvent.meetingStartTime} ~ ${meeting.upcomingEvent.meetingEndTime}`}
            </S.MeetingTimeSpan>
          </S.MeetingBox>
          <S.MeetingStatusSpan>{meetingStatusMessage}</S.MeetingStatusSpan>
        </S.Box>
      </S.MeetingItemLink>
    </S.Layout>
  );
};

export default MeetingItem;
