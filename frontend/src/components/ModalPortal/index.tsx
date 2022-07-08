import React, { useEffect, useRef, useState } from 'react';
import { createPortal } from 'react-dom';
import * as S from './ModalPortal.styled';

type ModalPortalProps = React.PropsWithChildren & {
  closePortal: React.MouseEventHandler;
};

const ModalPortal: React.FC<ModalPortalProps> = ({ children, closePortal }) => {
  const ref = useRef<Element | null>();
  const [mounted, setMounted] = useState(false);

  useEffect(() => {
    setMounted(true);

    if (document) {
      const dom = document.getElementById('root-modal');
      ref.current = dom;
    }
  }, []);

  if (ref.current && mounted) {
    return createPortal(
      <S.Layout>
        <S.ModalBackgroundBox onClick={closePortal} />
        {children}
      </S.Layout>,
      ref.current
    );
  }

  return null;
};
export default ModalPortal;
