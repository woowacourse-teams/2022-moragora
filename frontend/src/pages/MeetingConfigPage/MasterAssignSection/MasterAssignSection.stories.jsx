import { css } from '@emotion/react';
import MasterAssignSection from '.';

export default {
  title: 'Components/MasterAssignSection',
  component: MasterAssignSection,
};

function Template(args) {
  return (
    <div
      css={css`
        width: 100%;
      `}
    >
      <MasterAssignSection {...args} />
    </div>
  );
}

export const Default = Template.bind({});
