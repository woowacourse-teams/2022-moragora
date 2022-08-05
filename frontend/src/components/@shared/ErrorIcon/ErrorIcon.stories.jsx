import { css } from '@emotion/react';
import ErrorIcon from '.';

export default {
  title: 'Components/ErrorIcon',
  component: ErrorIcon,
};

function Template(args) {
  return (
    <div
      css={css`
        width: fit-content;
      `}
    >
      <ErrorIcon {...args} />
    </div>
  );
}

export const Default = Template.bind({});
