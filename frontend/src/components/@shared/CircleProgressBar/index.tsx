import * as S from './CircleProgressBar.styled';
import coffeeIcon from 'assets/beverage_icon.svg';

type CircleProgressBarProps = {
  size: number;
  percent: number;
};

const CircleProgressBar = ({ size, percent }: CircleProgressBarProps) => {
  return (
    <S.Layout>
      <S.SVG size={size}>
        <S.BackgroundBar cx={size / 2} cy={size / 2} r={size / 3} size={size} />
        <S.MeterBar
          cx={size / 2}
          cy={size / 2}
          r={size / 3}
          size={size}
          percent={percent}
        />
      </S.SVG>
      <S.CoffeeIcon size={size / 3} src={coffeeIcon} alt="coffee" />
    </S.Layout>
  );
};

export default CircleProgressBar;
