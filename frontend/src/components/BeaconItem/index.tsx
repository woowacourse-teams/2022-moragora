import { Beacon } from 'hooks/useKakaoMap';
import * as S from './BeaconItem.styled';

type BeaconProps = {
  id: Beacon['id'];
  position: Beacon['position'];
  address: Beacon['address'];
  radius: number;
  panTo: (latitude: number, longitude: number) => void;
  remove: (id: Beacon['id']) => void;
};

const BeaconItem = ({
  id,
  position,
  address,
  radius,
  panTo,
  remove,
}: BeaconProps) => {
  return (
    <S.Layout>
      <S.DescriptionBox>
        <span>{address.address_name}</span>
        <span>반경 {radius}m</span>
      </S.DescriptionBox>
      <S.ButtonBox>
        <S.Button
          type="button"
          onClick={() => {
            panTo(position.Ma, position.La);
          }}
        >
          <svg
            width="1.5rem"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke-width="1.5"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              d="M15 10.5a3 3 0 11-6 0 3 3 0 016 0z"
            />
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              d="M19.5 10.5c0 7.142-7.5 11.25-7.5 11.25S4.5 17.642 4.5 10.5a7.5 7.5 0 1115 0z"
            />
          </svg>
        </S.Button>
        <S.Button type="button" onClick={() => remove(id)}>
          <svg
            width="1.5rem"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke-width="1.5"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              d="M19.5 12h-15"
            />
          </svg>
        </S.Button>
      </S.ButtonBox>
    </S.Layout>
  );
};

export default BeaconItem;
