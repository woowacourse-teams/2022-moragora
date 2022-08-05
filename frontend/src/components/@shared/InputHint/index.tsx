import * as S from './InputHint.styled';

type InputHintProps = {
  isShow: boolean;
  message: string | null;
};

const InputHint: React.FC<InputHintProps> = ({ isShow, message }) => {
  return <>{isShow && <S.Paragraph>{message}</S.Paragraph>}</>;
};

export default InputHint;
