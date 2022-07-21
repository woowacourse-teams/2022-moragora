import React from 'react';
import * as S from './CoffeeStackItem.styled';
import CoffeeIconSVG from '../../assets/coffee.svg';
import { MeetingWithTardyCount } from 'types/meetingType';

type CoffeeStackItemProps = Pick<MeetingWithTardyCount, 'name' | 'tardyCount'>;

const CoffeeStackItem: React.FC<CoffeeStackItemProps> = ({
  name,
  tardyCount,
}) => {
  return (
    <S.Layout>
      <span>{name}</span>
      <S.Box>
        {Array.from({ length: tardyCount }).map((_, index) => (
          <S.CoffeeIconImage key={index} src={CoffeeIconSVG} />
        ))}
      </S.Box>
    </S.Layout>
  );
};

export default CoffeeStackItem;
