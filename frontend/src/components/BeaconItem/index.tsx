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
        <button
          type="button"
          onClick={() => {
            panTo(position.Ma, position.La);
          }}
        >
          이동
        </button>
        <button type="button" onClick={() => remove(id)}>
          제거
        </button>
      </S.ButtonBox>
    </S.Layout>
  );
};

export default BeaconItem;
